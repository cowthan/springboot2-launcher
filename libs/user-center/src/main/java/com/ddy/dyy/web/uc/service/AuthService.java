package com.ddy.dyy.web.uc.service;

import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.mybatis.base.BaseServiceImpl;
import com.ddy.dyy.web.lang.BeanUtils2;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.uc.mapper.UserMapper;
import com.ddy.dyy.web.uc.models.LoginResponse;
import com.ddy.dyy.web.uc.models.MyUserVO;
import com.ddy.dyy.web.uc.models.RegisterForm;
import com.ddy.dyy.web.uc.models.UserCreateBO;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * .
 */
@Service
public class AuthService extends BaseServiceImpl<UserMapper, UserEntity> {

    @Autowired
    private UserMapper dao;

    @Transactional
    public LoginResponse login(long appId, String account, String rawPassword, boolean checkPwd) {
        // 1 看账号密码对不对
        UserEntity user = dao.getByUsername(appId, account);
        if (user == null) {
            throw new LogicException(406, "账号未注册");
        } else {
            String password = AuthUtils.encodePassword(rawPassword, account, "");
            if (checkPwd && !password.equals(user.getPassword())) {
                throw new LogicException(406, "账号或密码错误");
            } else if (user.getStatus() != 1) {
                throw new LogicException(406, "账号已被封禁");
            } else if (user.getDeleted() != 0) {
                throw new LogicException(406, "账号已删除");
            }
        }

        // 5 生成token
        StpUtil.login(user.getId());

        // 填充返回字段
        LoginResponse loginResponse = BeanUtils2.copy(user, LoginResponse.class);
        loginResponse.setToken(StpUtil.getTokenValue());
        loginResponse.setTokenType("Bearer");

        MyUserVO userVO = BeanUtils2.copy(user, MyUserVO.class);
        loginResponse.setUserInfo(userVO);

        return loginResponse;
    }

    public UserEntity createAppUser(long appId, RegisterForm form) {
        String bigRole = "app";
        UserCreateBO userCreateBO = BeanUtils2.copy(form, UserCreateBO.class);
        // 添加用户
        UserEntity user = addUser(appId, bigRole, userCreateBO);
        return user;
    }

    public UserEntity addUser(long appId, String bigRole, UserCreateBO form) {
        form.setAppId(appId);
        form.setRole(bigRole);

        UserEntity user = dao.getByUsername(appId, form.getUsername());
        if (user != null && user.getDeleted() == 0) {
            throw new LogicException(406, "账号已存在");
        }
        user = buildNewUser(form);
        save(user);
        return user;
    }

    private UserEntity buildNewUser(UserCreateBO form) {
        UserEntity accountModel = BeanUtils2.copy(form, UserEntity.class);
        accountModel.setHeadIcon("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/1.webp");
        accountModel.setUid(AuthUtils.generateUUID());
        accountModel.setPassword(AuthUtils.encodePassword(form.getPassword(), form.getUsername(), ""));

        // 生成各种id，检查是否重复，入库
        long userCount = count();
        int sidLength = 4;
        if (userCount > 10000 / 2) sidLength = 5;
        if (userCount > 100000 / 2) sidLength = 6;
        if (userCount > 1000000 / 2) sidLength = 7; // 十万
        if (userCount > 10000000 / 2) sidLength = 8; // 百万
        if (userCount > 100000000 / 2) sidLength = 9; // 百万
        if (userCount > 1000000000 / 2) sidLength = 10; // 千万
        if (userCount > 1000000000) sidLength = 15;

        String sid = "";
        int sidCreateTimes = 0;
        while (Lang.isEmpty(sid)) {
            sidCreateTimes++;
            sid = AuthUtils.getRandomNumbers(sidLength);
            if (countBySid(sid) > 0) {
                sid = "";
            }
        }

        accountModel.setNickname("用户" + sid);
        accountModel.setSid(sid);
        return accountModel;
    }

    public UserEntity getByUsername(long appId, String username) {
        return dao.getByUsername(appId, username);
    }

    public UserEntity getByUid(String uid) {
        return dao.getByUid(uid);
    }

    public int countBySid(String sid) {
        return dao.countBySid(sid);
    }


}
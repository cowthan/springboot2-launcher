package com.ddy.dyy.web.uc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ddy.dyy.mybatis.base.BaseServiceImpl;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.uc.mapper.UserMapper;
import com.ddy.dyy.web.uc.models.admin.UserQuery;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceImpl<UserMapper, UserEntity> {

    @Autowired
    private UserMapper dao;

    public List<UserEntity> selectUserList(Long appId, UserQuery form) {
        return dao.selectUserList(appId, form);
    }

    public int deleteUserByIds(List<Long> ids) {
        if (Lang.isEmpty(ids)) return 0;
        return dao.deleteUserByIds(ids);
    }

    public boolean hasSameUsername(long appId, String bigRole, String username) {
        return hasSameUsername(appId, bigRole, username, 0);
    }

    public boolean hasSameUsername(long appId, String bigRole, String username, long excludeId) {
        UserEntity accountModel = dao.getByUsername(appId, username);
        if (accountModel != null && accountModel.getDeleted() == 1) accountModel = null;
        if (accountModel != null) {
            if (accountModel.getId().equals(excludeId)) return false;
            return true;
        }
        return false;
    }


    public Map<Long, UserEntity> getMapByUserIdList(List<Long> ids) {
        List<UserEntity> list = listByIds(ids);
        if (list == null) return new HashMap<>();
        return list.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
    }
}

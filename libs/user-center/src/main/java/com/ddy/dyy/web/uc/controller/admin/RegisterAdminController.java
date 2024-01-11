package com.ddy.dyy.web.uc.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddy.dyy.web.lang.Random2;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.models.biz.LongVo;
import com.ddy.dyy.web.uc.models.PickableUser;
import com.ddy.dyy.web.uc.models.UserCreateBO;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RegisterController
 */
@RestController
@RequestMapping("/admin/user/register")
public class RegisterAdminController {
    @Autowired
    private AuthService authService;
    private static Map<String, PickableUser> accounts = new HashMap<>();
    private static List<String> headIcons = new ArrayList<>();

    {
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/1.webp");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/2.webp");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/3.webp");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/4.webp");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/6.jpg");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/7.webp");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/8.jpg");
        headIcons.add("http://cowthan-public.oss-cn-qingdao.aliyuncs.com/mgchrn2023/headicons/9.jpg");
    }

    private static void createAccount(int count) {
        for (int i = 0; i < count; i++) {
            PickableUser user = new PickableUser();
            String code = Random2.getRandomNumbers(8);
            user.setAccount("123" + code);
            user.setNickname("临时账号" + code);
            user.setPassword("123456");
            user.setHeadIcon(headIcons.get(Random2.randomInt(headIcons.size()) % headIcons.size()));
            accounts.put(user.getAccount(), user);
        }
    }

    private static synchronized void makeIt40() {
        if (accounts.size() < 40) {
            createAccount(40 - accounts.size());
        }
    }

    @GetMapping("/list/account")
    public List<PickableUser> listAccount() {
        makeIt40();
        List<PickableUser> list = new ArrayList<>();
        list.addAll(accounts.values());
        return list;
    }

    @GetMapping("/pick")
    public LongVo pick(String account) {

        PickableUser pickableUser = accounts.remove(account);
        if (pickableUser == null) {
            throw new LogicException(406, "该账号已被抢占，请选择别的账号", null);
        }
        UserCreateBO userCreateBO = new UserCreateBO();
        userCreateBO.setUsername(pickableUser.getAccount());
        userCreateBO.setPassword(pickableUser.getPassword());
        userCreateBO.setHeadIcon(pickableUser.getHeadIcon());
        UserEntity user = authService.addUser(1, "app", userCreateBO);
        return LongVo.of(1);
    }
}

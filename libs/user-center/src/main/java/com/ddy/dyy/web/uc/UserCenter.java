package com.ddy.dyy.web.uc;

import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.web.lang.SpringContext;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.UserService;

public class UserCenter {
    public static UserEntity getCurrentUser() {
        Long userId = StpUtil.getLoginId(0L);
        if (userId > 0) {
            UserService userService = SpringContext.getBean(UserService.class);
            UserEntity user = userService.getById(userId);
            return user;
        }
        return null;
    }
}

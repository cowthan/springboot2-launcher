package com.ddy.dyy.web.uc.utils;

import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.lang.SpringContext;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.UserService;
import com.ddy.dyy.web.web.WebContext;


/**
 * AuthUtils
 */
public class AuthUtils {

    public static final String NUMBERS = "0123456789";
    static Random _random = new Random(System.currentTimeMillis());


    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static boolean isValidPassword(String password) {
        if (Lang.isEmpty(password) || password.length() < 6 || password.length() > 20) {
            return false;
        }
        return true;
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    public static String encodePassword(String rawPassword, String account, String secret) {
        if (Lang.isEmpty(rawPassword)) return "";
        return rawPassword;
    }

    public static String getRandom(String source, int length) {
        return (source == null || source.equalsIgnoreCase("")) ? null : getRandom(source.toCharArray(), length);
    }

    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            str.append(sourceChar[_random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    public static RequestData getRequestInfo() {
        return getRequestInfo(false);
    }

    public static RequestData getRequestInfo(boolean shouldLogin) {
        return getRequestInfo(shouldLogin, false);
    }

    public static RequestData getRequestInfo(boolean shouldLogin, boolean shouldHasAppId) {
        HttpServletRequest request = WebContext.currentRequest();

        // get app id
        String appId = request.getHeader("x-app-id");
        if (shouldHasAppId && appId == null) {
            throw new LogicException(406, "缺少header：x-app-id");
        }
        RequestData ret = new RequestData();
        try {
            ret.setAppId(Lang.toLong(appId));
        } catch (Exception e) {
            throw new LogicException(407, "非法的appid值");
        }
        if (shouldHasAppId && ret.getAppId() <= 0) {
            throw new LogicException(408, "必须携带合法的header x-app-id");
        }

        // get login user id
        Long loginId = StpUtil.getLoginId(0L);
        ret.setUserId(loginId);

        if (shouldLogin && ret.getUserId() <= 0) {
            throw new LogicException(401, "未登录");
        }

        // check app valid
        if (ret.getAppId() <= 0) {
            throw new LogicException(408, "app " + ret.getAppId() + " is in bad status");
        }

        // check user valid
        UserService userService = SpringContext.getBean(UserService.class);
        UserEntity user = null;
        if (ret.getUserId() > 0) {
            user = userService.getById(ret.getUserId());
            if (user == null || user.getStatus() != 1 || user.getDeleted() != 0) {
                throw new LogicException(401, "user is in bad status");
            }
        }
        return ret;
    }

}

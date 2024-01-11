package com.ddy.dyy.web.uc.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.models.biz.BooleanVo;
import com.ddy.dyy.web.uc.UserCenterRestController;
import com.ddy.dyy.web.uc.models.LoginResponse;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.admin.AdminUserProfileVo;
import com.ddy.dyy.web.uc.models.admin.LoginForm;
import com.ddy.dyy.web.uc.models.admin.RouterVo;
import com.ddy.dyy.web.uc.models.entity.AdminMenuEntity;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.AdminMenuService;
import com.ddy.dyy.web.uc.service.AuthService;
import com.ddy.dyy.web.uc.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@UserCenterRestController("/admin/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminAuthController {

    private final AuthService authService;
    private final AdminMenuService adminMenuService;

    @PostMapping("/login_pwd")
//    @AvoidRepeatRequestByIp(limit = 1000L)
    public Response<LoginResponse> login_pwd(@RequestBody @Valid LoginForm form) {
        RequestData r = AuthUtils.getRequestInfo(false, false);
        LoginResponse loginVO = authService.login(r.getAppId(), form.getUsername(),
                form.getPassword(), true);
        return Response.ok(loginVO);
    }

    @GetMapping("/profile")
    public Response<AdminUserProfileVo> getMyUserInfo() {
        RequestData r = AuthUtils.getRequestInfo(true, false);
        UserEntity user = authService.getById(r.getUserId());
        AdminUserProfileVo vo = new AdminUserProfileVo();
        vo.setRole(new String[]{user.getRole()});
        vo.setUid(user.getUid());
        vo.setId(r.getUserId());
        vo.setHeadIcon(Lang.snull(user.getHeadIcon(), ""));
        vo.setNickname(user.getNickname());
        return Response.ok(vo);
    }

    @PostMapping("/logout")
    public Response<BooleanVo> logout() {
        try {
            RequestData r = AuthUtils.getRequestInfo(true, false);
            StpUtil.logout();
        } catch (Exception e) {
            log.error("logout error but ignore", e);
        }
        return Response.ok(BooleanVo.TRUE);
    }

    @GetMapping("/captcha")
    public Response<Map> getCaptcha() {
        RequestData r = AuthUtils.getRequestInfo(false, false);
        Map ret = new HashMap<>();
        ret.put("uuid", "1111");
        ret.put("img", "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");
        return Response.ok(ret);
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/routers")
    public Response<List<RouterVo>> getRouters() {
        RequestData r = AuthUtils.getRequestInfo(true, false);
        UserEntity user = authService.getById(r.getUserId());
        AdminMenuEntity menu = adminMenuService.getOne(adminMenuService.lambdaWrapper()
                .eq(AdminMenuEntity::getAppId, r.getAppId())
                .eq(AdminMenuEntity::getRole, user.getRole())
                .eq(AdminMenuEntity::getStatus, 1));
        String json = menu.getMenus();
        List<RouterVo> menus = JsonUtils.toList(json, RouterVo.class);
        return Response.ok(menus);
    }
}

package com.ddy.dyy.web.uc.controller.admin;

import java.util.List;

import javax.validation.Valid;

import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.web.lang.BeanUtils2;
import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.models.biz.BooleanVo;
import com.ddy.dyy.web.uc.models.LoginResponse;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.UserCreateBO;
import com.ddy.dyy.web.uc.models.admin.AdminRegisterForm;
import com.ddy.dyy.web.uc.models.admin.AdminUserProfileVO;
import com.ddy.dyy.web.uc.models.admin.DevLoginForm;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminAuthController {

    private final AuthService authService;
    private final AdminMenuService adminMenuService;

    @PostMapping("/login_pwd")
//    @AvoidRepeatRequestByIp(limit = 1000L)
    public Response<LoginResponse> login_pwd(@RequestBody @Valid DevLoginForm form) {
        RequestData r = AuthUtils.getRequestInfo(false, false);
        LoginResponse loginVO = authService.login(r.getAppId(), form.getUsername(),
                form.getPassword(), true, "admin");
        return Response.ok(loginVO);
    }

    @PostMapping(value = "/register")
    public Response<LoginResponse> register(@RequestBody @Valid AdminRegisterForm form) {
        RequestData r = AuthUtils.getRequestInfo(false, false);
        UserCreateBO userCreateBO = BeanUtils2.copy(form, UserCreateBO.class);
        UserEntity user = authService.addUser(r.getAppId(), "admin", userCreateBO);

        LoginResponse loginVO = authService.login(0, form.getUsername(),
                form.getPassword(), false, "admin");
        return Response.ok(loginVO);
    }

    @GetMapping("/profile")
    public Response<AdminUserProfileVO> getMineUserInfo() {
        RequestData r = AuthUtils.getRequestInfo(true, false);
        UserEntity user = authService.getById(r.getUserId());
        AdminUserProfileVO vo = new AdminUserProfileVO();
        vo.setRole(new String[]{"admin"});
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

    /**
     * 获取路由信息
     */
    @GetMapping("/routers")
    public Response<List<RouterVo>> getRouters() {
        RequestData r = AuthUtils.getRequestInfo(true, false);
        AdminMenuEntity menu = adminMenuService.getOne(adminMenuService.lambdaWrapper()
                .eq(AdminMenuEntity::getAppId, r.getAppId())
                .eq(AdminMenuEntity::getBigRole, "admin")
                .eq(AdminMenuEntity::getStatus, 1));
        if (menu == null) {
            menu = adminMenuService.getOne(adminMenuService.lambdaWrapper()
                    .eq(AdminMenuEntity::getAppId, 0)
                    .eq(AdminMenuEntity::getBigRole, "admin")
                    .eq(AdminMenuEntity::getStatus, 1));
        }
        String json = menu.getMenus();
        List<RouterVo> menus = JsonUtils.toList(json, RouterVo.class);
        return Response.ok(menus);
    }
}

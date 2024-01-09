package com.ddy.dyy.web.uc.controller.admin;

import java.util.List;

import com.ddy.dyy.mybatis.models.PageList;
import com.ddy.dyy.mybatis.models.PageRequest;
import com.ddy.dyy.mybatis.page.PageUtils;
import com.ddy.dyy.web.lang.BeanUtils2;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.models.biz.LongVo;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.UserCreateBO;
import com.ddy.dyy.web.uc.models.admin.ChangeStatusForm;
import com.ddy.dyy.web.uc.models.admin.PwdResetForm2;
import com.ddy.dyy.web.uc.models.admin.RegisterForm2;
import com.ddy.dyy.web.uc.models.admin.UserEditForm;
import com.ddy.dyy.web.uc.models.admin.UserQuery1;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.AuthService;
import com.ddy.dyy.web.uc.service.UserService;
import com.ddy.dyy.web.uc.utils.AuthUtils;
import com.ddy.dyy.web.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private void checkRowExistsAndIsOwnedByThisApp(long appId, long appUserId) {
        UserEntity row = userService.getById(appUserId);
        if (row != null && row.getAppId().equals(appId)) {
            return;
        }
        throw new LogicException(406, "app user " + appUserId + " is not belong to app " + appId);
    }

    @GetMapping("/list")
    public Response<PageList> list(@Validated UserQuery1 form, @Validated PageRequest pageForm) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        PageUtils.startPage(pageForm);
        form.setBigRole("app");
        List<UserEntity> list = userService.selectUserList(requestData.getAppId(), form);
        PageList<UserEntity> pageData = PageUtils.parse(list);
        return Response.ok(new PageList(pageData.getList(), pageData.getTotalCount(), pageForm.getSize()));
    }

    @GetMapping("/detail_for_edit")
    public Response<UserEditForm> detail_for_edit(@RequestParam Long id) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        checkRowExistsAndIsOwnedByThisApp(requestData.getAppId(), id);
        UserEntity UserEntity = userService.getById(id);
        UserEditForm r = BeanUtils2.copy(UserEntity, UserEditForm.class);
        return Response.ok(r);
    }

    //@OpLog(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Response<LongVo> add(@Validated @RequestBody RegisterForm2 form) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        String bigRole = "app";
        UserCreateBO userCreateBO = BeanUtils2.copy(form, UserCreateBO.class);
        UserEntity user = authService.addUser(requestData.getAppId(), bigRole, userCreateBO);
        return Response.ok(LongVo.of(user.getId()));
    }

    //@OpLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public Response<LongVo> edit(@Validated @RequestBody UserEditForm form) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        checkRowExistsAndIsOwnedByThisApp(requestData.getAppId(), form.getId());

        UserEntity a2 = new UserEntity().setId(form.getId())
                .setNickname(form.getNickname())
                .setHeadIcon(form.getHeadIcon())
                .setGender(form.getGender());
        boolean r = userService.updateById(a2);
        return Response.ok(LongVo.of(1));
    }

    //@OpLog(title = "用户管理", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Response<LongVo> delete(@RequestParam String ids) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        List<Long> userIds = Lang.splitToLongList(ids, ",");
        for (Long id : userIds) {
            checkRowExistsAndIsOwnedByThisApp(requestData.getAppId(), id);
        }
        int r = userService.deleteUserByIds(userIds);
        return Response.ok(LongVo.of(r));
    }

   // @OpLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public Response<LongVo> resetPwd(@RequestBody @Validated PwdResetForm2 form) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        checkRowExistsAndIsOwnedByThisApp(requestData.getAppId(), form.getId());

        UserEntity a2 = new UserEntity()
                .setId(form.getId())
                .setPassword(AuthUtils.encodePassword(form.getPassword(), null, ""));
        boolean r = authService.updateById(a2);
        return Response.ok(LongVo.of(r));
    }

   // @OpLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public Response<LongVo> changeStatus(@RequestBody @Validated ChangeStatusForm form) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        checkRowExistsAndIsOwnedByThisApp(requestData.getAppId(), form.getId());

        boolean r = userService.updateById(new UserEntity().setId(form.getId()).setStatus(form.getStatus()));
        return Response.ok(LongVo.of(r));
    }
}

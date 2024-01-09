package com.ddy.dyy.web.uc.controller.admin;

import java.io.IOException;

import com.ddy.dyy.web.lang.BeanUtils2;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.models.biz.BooleanVo;
import com.ddy.dyy.web.uc.models.MyUserVO;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.admin.AdminUserForm;
import com.ddy.dyy.web.uc.models.admin.PwdUpdateForm2;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.AuthService;
import com.ddy.dyy.web.uc.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * .
 */
@Slf4j
@RestController
@RequestMapping("/admin/profile")
public class AdminProfileController {

    @Autowired
    private AuthService authService;


    /**
     * 个人信息
     */
    @GetMapping("/get")
    public Response<MyUserVO> profile() {
        RequestData requestData = AuthUtils.getRequestInfo(true, false);
        UserEntity profile = authService.getById(requestData.getUserId());
        return Response.ok(BeanUtils2.copy(profile, MyUserVO.class));
    }

    /**
     * 修改用户
     */
    @PostMapping("/edit")
    public Response<BooleanVo> updateProfile(@RequestBody AdminUserForm user) {
        RequestData requestData = AuthUtils.getRequestInfo(true, false);
        UserEntity row = BeanUtils2.copy(user, UserEntity.class);
        row.setId(requestData.getUserId());
        authService.updateById(row);
        return Response.ok(BooleanVo.TRUE);

    }

    @PostMapping("/pwd_update")
    public Response<BooleanVo> updatePassword(@RequestBody @Validated PwdUpdateForm2 form) {
        RequestData requestData = AuthUtils.getRequestInfo(true, false);
        UserEntity accountModel = authService.getById(requestData.getUserId());
        String oldPassword = AuthUtils.encodePassword(form.getOldPassword(), accountModel.getUsername(), "");
        if (!oldPassword.equals(accountModel.getPassword())) {
            throw new LogicException(406, "旧密码错误");
        }

        String newPassword = AuthUtils.encodePassword(form.getNewPassword(), accountModel.getUsername(), "");
        UserEntity a2 = new UserEntity().setId(accountModel.getId()).setPassword(newPassword);
        authService.updateById(a2);
        return Response.ok(BooleanVo.TRUE);
    }


    /**
     * 头像上传
     */
//    @PostMapping("/avatar")
//    public FileUploadVO avatar(MultipartFile file) {
//        RequestData requestData = AuthUtils.getRequestInfo(true, false);
//        if (!file.isEmpty()) {
//
//            FileUploadVO vo;
//            try {
//                OssConfig ossConfig = FileHandlerSupport.getDevOssConfig();
//                FileHandler fileHandler = FileHandlerSupport.getOssHandler(ossConfig);
//                String suffix = Lang.snull(Lang.getFileSuffix(file.getOriginalFilename()), "png");
//                String prefix = ossConfig.getDir() + requestData.getUserId() + "/avatar";
//                vo = FileHandlerSupport.upload(fileHandler, prefix, file.getBytes(), suffix);
//            } catch (IOException e) {
//                log.error("上传文件失败", e);
//                throw new LogicException(406, "上传文件失败", null);
//            }
//            UserEntity row = new UserEntity().setId(requestData.getUserId()).setHeadIcon(vo.getUrl());
//            authService.updateById(row);
//            return vo;
//
//        }
//        throw new LogicException(406, "上传图片异常，请联系管理员");
//    }
}

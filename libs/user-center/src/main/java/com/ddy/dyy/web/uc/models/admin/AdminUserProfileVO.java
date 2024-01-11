package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;

@Data
public class AdminUserProfileVO {
    private Long id;
    private String nickname;
    private String headIcon;
    private Integer gender;
    private String uid;
    private String phone = "未知";
    private String email = "未知";
    private String[] role;
    private String[] permissions = new String[]{"*:*:*"};
}
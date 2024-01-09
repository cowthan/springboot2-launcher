package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;

@Data
public class AdminUserProfileVO {
    private Long id;
    private String nickname;
    private String headIcon;
    private String uid;
    private String[] role;
    private String[] permissions = new String[]{"*:*:*"};


}
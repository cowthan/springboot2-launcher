package com.ddy.dyy.web.uc.models;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String tokenType;

    private String uid;
    private String sid;
    private String appRole;

    private MyUserVo userInfo;
}

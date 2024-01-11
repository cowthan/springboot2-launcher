package com.ddy.dyy.web.uc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateBO {

    private Long id;
    private Long appId;
    private String username;
    private String password;
    private String role;
    private Integer gender;
    private String nickname;
    private String headIcon;
    private String signature;
    private String birth;
    private String extra;

}
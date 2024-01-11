package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private String uuid;
    private String code;
}

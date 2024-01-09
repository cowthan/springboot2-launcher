package com.ddy.dyy.web.uc.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterForm {

    private String username;

    private String password;

//    @Length(min = 1, max = 9)
    private String code;

//    private Long auth3Id;

    private String nickname;

    private Integer gender = 0;

    private String headIcon;

    private String appRole;
}

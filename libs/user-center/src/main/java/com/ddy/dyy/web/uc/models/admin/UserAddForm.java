package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;

@Data
public class UserAddForm {

    //    @CanNotBeEmpty
//    @AccountValue
    private String username;

    //    @CanNotBeEmpty
//    @PasswordValue
    private String password;

    //    @CanNotBeEmpty
    private String nickname;

    private Integer gender = 0;

    private String headIcon;

    private String appRole;
}

package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AdminRegisterForm {

//    @CanNotBeNull
//    @AccountValue
    private String username;


//    @CanNotBeNull
//    @PasswordValue
    private String password;

//    @CanNotBeNull
    @Length(min = 4, max = 6)
    private String code;

//    @CanNotBeNull
    @Length(min = 2, max = 30)
    private String name;

}

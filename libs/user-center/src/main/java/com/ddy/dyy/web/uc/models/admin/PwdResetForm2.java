package com.ddy.dyy.web.uc.models.admin;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class PwdResetForm2 {

    //    @CanNotBeNull
    @Min(1)
    private Long id;

    //    @CanNotBeEmpty
//    @PasswordValue
    private String password;


}



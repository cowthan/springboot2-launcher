package com.ddy.dyy.web.uc.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginForm {

    private String username;
    private String password;
    @Length(min = 1, max = 9)
    private String code;
//    private Long auth3Id;
}

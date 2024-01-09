package com.ddy.dyy.web.uc.models.admin;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class UserEditForm {

    //    @CanNotBeNull
    @Min(1)
    private Long id;

    //    @CanNotBeEmpty
    private String nickname;

    private Integer gender = 0;

    private String headIcon;

}

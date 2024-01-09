package com.ddy.dyy.web.uc.models.admin;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class ChangeStatusForm {

    //    @CanNotBeNull
    @Min(1)
    private Long id;

    //    @CanNotBeNull
    @Min(0)
    private Integer status;


}



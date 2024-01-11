package com.ddy.dyy.web.uc.models;

import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class MyUserVo {

    @Length(min = 2, max = 64)
    private String nickname;
    private String username;
    private String headIcon;
    @Length(min = 0, max = 256)
    private String signature;
    @Range(min = 0, max = 2)
    private Integer gender;
    private String phone = "未知";
    private String email = "未知";
    private Date gmtCreate;
}
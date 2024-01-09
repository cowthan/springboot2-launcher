package com.ddy.dyy.web.uc.models.admin;

import java.util.Date;

import lombok.Data;

/**
 * @author danger
 * @date 2021-07-11
 */

@Data
public class MainAppVO{

    private Long id;


    private Long userId;


    private String name;


    private String appKey;


    private String appSecretKey;


    private String summary;


    private Integer status;


    private Integer deleted;


    private Date gmtCreate;


    private Date gmtModified;


}

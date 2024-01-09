package com.ddy.dyy.web.uc.models.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用对象 t_user_app
 * 
 * @author danger
 * @date 2021-07-11
 */
@Data
@TableName("t_user_app")
public class MainAppEntity {

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

package com.ddy.dyy.web.uc.models.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_admin_menu")
public class AdminMenuEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appId;
    private String role;
    private String menus;
    private Integer status;
    private Date gmtCreate;
    private Date gmtModified;
}
package com.ddy.dyy.web.uc.models.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appId;
    private String username;
    private String password;
    private String uid;
    private String sid;
    private String bigRole;
    private String appRole;
    private String adminRole;
    private Integer gender;
    private String nickname;
    private String headIcon;
    private Integer status;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
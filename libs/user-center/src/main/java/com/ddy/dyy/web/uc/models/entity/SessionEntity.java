package com.ddy.dyy.web.uc.models.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user_session")
public class SessionEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appId;
    private String k;
    private String v;
    private Long dieTime;
    private Long version;
    private Date gmtCreate;
    private Date gmtModified;
}
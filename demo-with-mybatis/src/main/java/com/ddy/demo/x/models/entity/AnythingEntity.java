package com.ddy.demo.x.models.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 */
@Getter
@Setter
@TableName("t_anything")
public class AnythingEntity {
    @TableId(type = IdType.AUTO) // 如果没有这个，会从很大的一个数开始自增，并且不见得是递增
    private Long id;
    private String code;
    private String type;
    private String keyword;
    private String content;
    private Integer status;
    private Integer version;
    private Long isDelete;
    private Date createAt;
    private Date updateAt;
    private Date deleteAt;
}

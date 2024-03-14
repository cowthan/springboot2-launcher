package com.danger.t7.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity {
    @TableId
    private Long id;
    private String name;
    private Integer age;
    private String email;
}


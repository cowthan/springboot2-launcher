package com.danger.t7.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能描述
 */
@Data
@Schema
public class UserEntity2 {
    @Schema(description = "字段描述")
    private Integer userId;

    @Schema(title = "用户名称")
    private String username;

    @Schema(title = "用户密码")
    private String password;

    @Schema(title = "用户手机")
    private String mobile;

    public UserEntity2() {

    }

    public UserEntity2(Integer userId, String username, String password, String mobile) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }
}

package com.danger.t7.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能描述
 */
@Data
@Schema(title = "user实体", name = "user实体name")
public class UserEntity {
    @Schema(title = "用户ID", name = "userId用户id", description = "字段描述")
    private Integer userId;

    @Schema(title = "用户名称")
    private String username;

    @Schema(title = "用户密码")
    private String password;

    @Schema(title = "用户手机")
    private String mobile;

    public UserEntity() {

    }

    public UserEntity(Integer userId, String username, String password, String mobile) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }
}

package com.danger.t7.controller;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能描述
 */
@Data
@Schema
public class UserRequest {
//    @ApiModelProperty(name = "userId", value = "这个是ApiModelProperty修饰的")
    private Integer userId;

    @Parameter(description = "这是个aaa参数 -- 挺好")
    @Schema(description = "这是个aaa参数 -- 没用滴")
    private String aaa;

    @Schema(description = "这是个aaa参数 on getter -- 没用滴")
    public String getAaa() {
        return aaa;
    }
}

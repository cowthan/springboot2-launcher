package com.ddy.dyy.web.uc.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

//@ApiModel("用户基本信息实体")
@Data
public class HerUserVO {

//    @CanNotBeNull
    @Length(min = 2, max = 64)
//    @ApiModelProperty("昵称，最长64")
    private String nickname;

//    @CanNotBeNull
//    @UrlValue
//    @ApiModelProperty("头像url")
    private String headIcon;

//    @CanNotBeNull
    @Length(min = 0, max = 256)
//    @ApiModelProperty("签名，最长256")
    private String signature;

//    @CanNotBeNull
    @Range(min = 0, max = 2)
//    @ApiModelProperty("性别，0未知，1男，2女")
    private Integer gender;

//    @CanNotBeNull
//    @Length(min = 0, max = 100)
//    @ApiModelProperty("邮箱")
//    private String email;
//
//    @CanNotBeNull
//    @ApiModelProperty("年龄")
//    private Integer age;
//
//    @CanNotBeNull
//    @ApiModelProperty("生日")
//    private String birth;
//
//    @CanNotBeNull
//    @ApiModelProperty("用户mobile")
//    private String mobile;
//
//    @CanNotBeNull
//    @ApiModelProperty("地址")
//    private String address;
//
//    @ApiModelProperty("纬度")
//    private Double lat;
//
//    @ApiModelProperty("经度")
//    private Double lon;
//
//    @ApiModelProperty("附属信息")
//    private String extra;

}
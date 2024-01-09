package com.ddy.dyy.web.uc.models;

import lombok.Data;

@Data
public class HerUserDetail {

    private String uid;
    private String sid;
    private String appRole;

    private HerUserVO userInfo;
}

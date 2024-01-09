package com.ddy.dyy.web.uc.models;

import lombok.Data;

@Data
public class AppUserDetail {

    private String uid;
    private String sid;
    private String appRole;

    private MyUserVO userInfo;
}

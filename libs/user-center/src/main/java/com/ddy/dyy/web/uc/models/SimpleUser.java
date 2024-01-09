package com.ddy.dyy.web.uc.models;

import lombok.Data;

@Data
public class SimpleUser {

    private String uid;
    private String nickname;
    private String avatar;

    public SimpleUser(String uid, String nickname, String avatar) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public SimpleUser() {
    }
}

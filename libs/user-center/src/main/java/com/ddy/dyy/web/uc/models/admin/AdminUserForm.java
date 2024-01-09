package com.ddy.dyy.web.uc.models.admin;

import lombok.Data;

@Data
public class AdminUserForm {
    private Long id;
    private String username;
    private String nickname;
    private int gender;


}
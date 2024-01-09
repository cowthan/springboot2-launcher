package com.ddy.dyy.web.uc.models;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * SessionAdmin
 */
public class SessionAdmin {

    @Getter
    @Setter
    public static class ListRequest {
        private String keyword;
    }

    @Getter
    @Setter
    public static class ItemVo {
        private Long id;
        private String username;
        private String k;
        private String token;
        private String device;
        private String v;
        private Date dieTime;
        private Date gmtCreate;
        private SimpleUser user;
    }


}

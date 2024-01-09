package com.ddy.dyy.web.uc.models;

import lombok.Data;

/**
 * 存放请求链上需要共享的数据
 */
@Data
public class RequestData {
    private long appId;
    private long userId;
    private String requestId;
}

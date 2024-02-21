package com.danger.demo.d007_封装.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class MessageWrapper implements Serializable {

    private String messageId;

    private long len;

    // inbound时，收到的都是ByteBuf
    // outbound时，writeAndFlush的是什么就是什么，不一定是ByteBuf
    private Object msg;

}

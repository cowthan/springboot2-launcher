package com.danger.demo;

import com.danger.netty.ConnectionManager;
import com.danger.netty.NettyLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BaseClientChannelHandler extends ChannelInboundHandlerAdapter {

    private void log(String msg){
        NettyLog.log("client - [" + this.getClass().getSimpleName() + "] " + msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log("-------------handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log("-------------handlerRemoved");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log("-------------channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log("-------------channelInactive");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log("-------------channelRegistered");
        ConnectionManager.getDefault().onConnect(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log("-------------channelUnregistered");
        ConnectionManager.getDefault().onDisconnect(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log("-------------channelRead");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log("-------------channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log("-------------exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}

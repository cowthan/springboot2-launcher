package com.danger.demo.d009_1;

import com.danger.demo.d007_封装.sender.MessageSender;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 类功能描述
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("server receives message: " + msg.trim());

        String host = ctx.channel().remoteAddress().toString();
        MessageSender.sendMessage("hello client!", NettyConnection.BizKeyType.DEFAULT, host, new NettySupport.OperationCallback() {
            @Override
            public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {

            }
        });

    }
}
package com.danger.demo.d009_1;

import com.danger.netty.ConnectionManager;
import com.danger.netty.NettySupport;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 类功能描述
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("client receives message: " + msg.trim());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ConnectionManager.getDefault().onConnect(ctx);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("准备发送2....");
                    System.out.println("发送，使用：" + ctx.hashCode());
                    NettySupport.waitInLoop(3);
                    ctx.writeAndFlush("hello server!");

                }
            }
        }).start();
    }
}

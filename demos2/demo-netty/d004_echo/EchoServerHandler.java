package com.danger.demo.d004_echo;

import com.danger.demo.BaseServerChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends BaseServerChannelHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)

        // or use writeAndFlush for brevity.
    }
}

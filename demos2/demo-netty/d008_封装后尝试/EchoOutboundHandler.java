package com.danger.demo.d008_封装后尝试;

import com.danger.netty.handler.BaseOutboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
public class EchoOutboundHandler extends BaseOutboundHandler {

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        log("----flush - ");
        super.read(ctx);
    }



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log("----write - " + msg.getClass().getSimpleName());
        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        log("----flush");
        super.flush(ctx);
    }

}

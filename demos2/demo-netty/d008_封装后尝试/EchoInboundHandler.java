package com.danger.demo.d008_封装后尝试;

import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class EchoInboundHandler extends BaseInboundHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // ByteBuf to String
        String body = NettySupport.getString((ByteBuf) msg);

        ((ByteBuf) msg).release();

        //forward
        super.channelRead(ctx, body);
    }
}

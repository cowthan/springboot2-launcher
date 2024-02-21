package com.danger.demo.d008_封装后尝试.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class EchoEncoder extends MessageToByteEncoder<EchoProtocol> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded -- " + getClass().getSimpleName());
        super.handlerAdded(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EchoProtocol msg, ByteBuf out) throws Exception {
        System.out.println("================encoder==================");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent().getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(out);
    }
}
package com.danger.demo.d008_封装后尝试;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ChannelHandler.Sharable
public class EchoInboundHandler2 extends StringDecoder {

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        // ByteBuf to String
//        String body = NettySupport.getString((ByteBuf) msg);
//        log("收到：" + body);
//        ((ByteBuf) msg).release();
//
//        //forward
//        super.channelRead(ctx, body);
//    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("-------decode - StringDecoder");
        out.add(msg.toString(StandardCharsets.UTF_8));
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("-------channelRead - StringDecoder");
//        super.channelRead(ctx, msg);
//    }
}

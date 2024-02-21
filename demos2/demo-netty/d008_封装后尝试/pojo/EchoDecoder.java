package com.danger.demo.d008_封装后尝试.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class EchoDecoder extends ReplayingDecoder<Void> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded -- " + getClass().getSimpleName());
        super.handlerAdded(ctx);
    }

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
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("================decoder==================");
        int length = in.readInt();

        byte[] content = new byte[length];
        in.readBytes(content);

        EchoProtocol protocol = new EchoProtocol();
        protocol.setLength(length);
        protocol.setContent(new String(content, StandardCharsets.UTF_8));

        out.add(protocol);
    }

    //    @Override
    //    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //        System.out.println("-------channelRead - StringDecoder");
    //        super.channelRead(ctx, msg);
    //    }
}
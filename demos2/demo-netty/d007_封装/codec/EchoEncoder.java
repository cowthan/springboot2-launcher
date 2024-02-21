package com.danger.demo.d007_封装.codec;

import com.danger.demo.d007_封装.wrapper.MessageWrapper;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import com.danger.netty.handler.BaseOutboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * EchoMsg to bytebuf
 */
@ChannelHandler.Sharable
public class EchoEncoder extends BaseOutboundHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log("-------write");
        MessageWrapper messageWrapper = (MessageWrapper) msg;
        EchoMsg echoMsg = (EchoMsg) messageWrapper.getMsg();

//        String bodyJson = JsonUtils.toJson(echoMsg);
//        ByteBuf body = NettySupport.getByteBuf(bodyJson);
//
//        messageWrapper.setMsg(body);
//        messageWrapper.setLen(body.readableBytes());

        // TODO do traffic stat with MessageWrapper
//        super.write(ctx, body, promise);
    }

}

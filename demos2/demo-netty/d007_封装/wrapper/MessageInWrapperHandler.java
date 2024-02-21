package com.danger.demo.d007_封装.wrapper;

import com.danger.netty.handler.BaseInboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * ByteBuf to MessageWrapper
 */
@ChannelHandler.Sharable
public class MessageInWrapperHandler extends BaseInboundHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log("-------onChannelRead");
        ByteBuf byteBuf = (ByteBuf) msg;
        MessageWrapper messageWrapper = new MessageWrapper();

        messageWrapper.setMsg(byteBuf);
        messageWrapper.setMessageId("");
        messageWrapper.setLen(byteBuf.readableBytes());
        super.channelRead(ctx, messageWrapper);
    }
}

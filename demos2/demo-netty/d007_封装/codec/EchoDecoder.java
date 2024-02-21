package com.danger.demo.d007_封装.codec;

import com.danger.demo.d007_封装.wrapper.MessageWrapper;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/*
 * bytebuf to EchoMsg
 */
@ChannelHandler.Sharable
public class EchoDecoder extends BaseInboundHandler {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log("-------onChannelRead");
        MessageWrapper messageWrapper = (MessageWrapper) msg;
        String cmd = NettySupport.getString((ByteBuf) messageWrapper.getMsg());
//        EchoMsg echoMsg = JsonUtils.toObj(cmd, EchoMsg.class);
//        System.out.println("收到:" + echoMsg.getPayload());
//
//        messageWrapper.setMessageId(echoMsg.getMessageId());
//        messageWrapper.setMsg(echoMsg);

        // TODO do traffic stat with MessageWrapper

        super.channelRead(ctx, messageWrapper);
    }
}

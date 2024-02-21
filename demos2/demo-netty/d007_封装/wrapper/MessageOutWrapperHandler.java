package com.danger.demo.d007_封装.wrapper;

import com.danger.demo.d007_封装.codec.EchoMsg;
import com.danger.netty.handler.BaseOutboundHandler;
import io.netty.channel.*;

@ChannelHandler.Sharable
public class MessageOutWrapperHandler extends BaseOutboundHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log("-------write");
        EchoMsg echoMsg = (EchoMsg) msg;
        MessageWrapper messageWrapper = new MessageWrapper();

        messageWrapper.setMsg(msg);
        messageWrapper.setMessageId(echoMsg.getMessageId());
        messageWrapper.setLen(0);

        super.write(ctx, messageWrapper, promise);
    }

}

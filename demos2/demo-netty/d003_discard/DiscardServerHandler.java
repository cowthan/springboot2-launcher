package com.danger.demo.d003_discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        // ByteBuf is a reference-counted object which has to be released explicitly via the release() method.
//        ((ByteBuf) msg).release(); // (3)

        // 一般这么处理，主要是为了在ByteBuf使用完之后能释放
        try {

            // Do something with msg
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // when an exception is raised.
        // - 1 应该记录日志
        // - 2 应该Close the connection
        // - 3 可以在close之前给对方发一个error code
        cause.printStackTrace();
        ctx.close();
    }
}
package com.danger.demo.d006_拆和粘;

import com.danger.demo.BaseServerChannelHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端负责发QUERY TIME ORDER指令
 * 服务端读到一条消息，就计数一次，然后发应答给客户端
 * 理论上，服务端收到的消息数和客户端发送的消息数应该相等
 */
@ChannelHandler.Sharable
public class TimerServerHandler extends BaseServerChannelHandler {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

    }
}

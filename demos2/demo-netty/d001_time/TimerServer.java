package com.danger.demo.d001_time;

import com.danger.demo.BaseServerChannelHandler;
import com.danger.netty.NettySupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerServer {

    public static void main(String[] args) {
        NettySupport.startServer(true, 7222, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChildChannelHandler());
            }
        });
    }

    @ChannelHandler.Sharable
    private static class ChildChannelHandler extends BaseServerChannelHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            ByteBuf buf = (ByteBuf) msg;
            String body = NettySupport.getString(buf);
            System.out.println("server: receive order - " + body);

            String response = "";
            if("query time order".equalsIgnoreCase(body)){
                response = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }else{
                response = "BAD ORDER - " + body;
            }

            System.out.println("server: start send response - " + response);
            NettySupport.sendTo(ctx, NettySupport.getByteBuf(response), new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {
                }
            });
        }
    }

}


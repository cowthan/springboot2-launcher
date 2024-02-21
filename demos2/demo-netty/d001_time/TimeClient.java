package com.danger.demo.d001_time;

import com.danger.demo.BaseClientChannelHandler;
import com.danger.netty.NettySupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeClient {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(4000L);

        NettySupport.startClient(true, "localhost", 7222, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new TimeClientHandler());
            }
        });

    }

    private static class TimeClientHandler extends BaseClientChannelHandler {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            String s = "QUERY TIME ORDER";
            System.out.println("client: send to server - " + s);
            NettySupport.sendTo(ctx, NettySupport.getByteBuf(s), new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {

                }
            });
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            ByteBuf buf = (ByteBuf) msg;
            String body = NettySupport.getString(buf);
            System.out.println("client: receive - " + body);

            ctx.close();
        }

    }
}

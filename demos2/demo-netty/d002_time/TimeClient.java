package com.danger.demo.d002_time;

import com.danger.demo.BaseClientChannelHandler;
import com.danger.netty.ConnectionManager;
import com.danger.netty.NettySupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeClient {

//    public static void main(String[] args) throws InterruptedException {
//        NettySupport.waitInLoop(3, true);
//        TimeClient client = new TimeClient();
//        client.start("localhost", 7222);
//
//        // 阻塞住主线程
//        NettySupport.waitInLoop();
//    }
//
//    private String serverName;
//
//    public void start(String host, int port){
//        serverName = host + ":" + port;
//        NettySupport.startClientInThread(host, port, new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel socketChannel) throws Exception {
//                socketChannel.pipeline().addLast(new TimeClientHandler());
//                socketChannel.pipeline().addLast(new TimeClientHandler(){});
//            }
//        });
//    }
//
//    public void sendToServer(String s){
//        if(!ConnectionManager.getDefault().isOnline(serverName)){
//            System.out.println("服务端不在线...");
//            return;
//        }
//
//        ChannelHandlerContext ctx = ConnectionManager.getDefault().getByBizKey(serverName);
//        NettySupport.sendTo(ctx, NettySupport.getByteBuf(s), new NettySupport.OperationCallback() {
//            @Override
//            public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {
//            }
//        });
//    }
//
//    private class TimeClientHandler extends BaseClientChannelHandler {
//
//        @Override
//        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//            super.channelRegistered(ctx);
//            ConnectionManager.getDefault().bindToBusiness(serverName, ctx);
//        }
//
//        @Override
//        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            super.channelRead(ctx, msg);
//            ByteBuf buf = (ByteBuf) msg;
//            String body = NettySupport.getString(buf);
//            System.out.println("client: receive - " + body);
//        }
//
//    }
}

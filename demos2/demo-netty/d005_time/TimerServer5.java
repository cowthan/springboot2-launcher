package com.danger.demo.d005_time;

import com.danger.demo.BaseServerChannelHandler;
import com.danger.netty.ConnectionManager;
import com.danger.netty.NettySupport;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerServer5 {

    public static void start(int port) {
        NettySupport.startServerInThread(port, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChildChannelHandler());
            }
        });

    }

    private static void sendCurrentTimeToClient(String bizKey){
//        if(!ConnectionManager.getDefault().isOnline(bizKey)){
//            System.out.println("客户端不在线...");
//            return;
//        }
        String response = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        System.out.println("server: send response - " + response);

//        ChannelHandlerContext ctx = ConnectionManager.getDefault().getByBizKey(bizKey);
//        NettySupport.sendTo(ctx, NettySupport.getByteBuf(response), new NettySupport.OperationCallback() {
//            @Override
//            public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {
//            }
//        });
    }

    @ChannelHandler.Sharable
    private static class ChildChannelHandler extends BaseServerChannelHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            String bizKey = ctx.channel().remoteAddress().toString();
//            ConnectionManager.getDefault().bindToBusiness(bizKey, ctx);

            sendCurrentTimeToClient(bizKey);
        }

    }

}


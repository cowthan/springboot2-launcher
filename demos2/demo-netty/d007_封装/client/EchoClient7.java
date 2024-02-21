package com.danger.demo.d007_封装.client;

import com.danger.demo.d007_封装.EchoHandlers;
import com.danger.demo.d007_封装.codec.EchoMsg;
import com.danger.demo.d007_封装.sender.MessageSender;
import com.danger.netty.BaseClient;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class EchoClient7 extends BaseClient {

    private String serverName;

    public EchoClient7(String host, int port) {
        super( host, port);
        serverName = "server-for-" + this.hashCode() + "-" + System.currentTimeMillis();
    }

    @Override
    protected List<ChannelHandler> getHandlers() {
        return EchoHandlers.getClientHandlers(serverName);
    }

    @ChannelHandler.Sharable
    public static class EchoClientHandler extends BaseInboundHandler{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log("-----channelRead");
//            System.out.println("收到1:" + JsonUtils.toJson(msg));
            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) {

        EchoClient7 client = new EchoClient7("10.113.98.37", 7222);
        client.start();

        while (true){
            NettySupport.waitInLoop(10, false);
            EchoMsg msg = new EchoMsg(System.currentTimeMillis() + "-from-client");
            MessageSender.sendMessage(msg, NettyConnection.BizKeyType.ACCOUNT, client.serverName, new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {

                }
            });
        }


    }

}

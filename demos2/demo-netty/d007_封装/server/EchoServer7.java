package com.danger.demo.d007_封装.server;

import com.danger.demo.d007_封装.EchoHandlers;
import com.danger.demo.d007_封装.sender.MessageSender;
import com.danger.demo.d007_封装.wrapper.MessageWrapper;
import com.danger.demo.d007_封装.codec.EchoMsg;
import com.danger.netty.BaseServer;
import com.danger.netty.ConnectionManager;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class EchoServer7 extends BaseServer {
    public EchoServer7(int port) {
        super(port);
    }

    @Override
    protected List<ChannelHandler> getHandlers() {
        return EchoHandlers.getServerHandlers();
    }

    @ChannelHandler.Sharable
    public static class EchoServerHandler extends BaseInboundHandler{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log("-----channelActive");
            String host = ctx.channel().remoteAddress().toString();
            ConnectionManager.getDefault().bindToBusiness(ctx, NettyConnection.BizKeyType.ACCOUNT, host);
            super.channelActive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            MessageWrapper messageWrapper = (MessageWrapper) msg;
            EchoMsg echoMsg = (EchoMsg) messageWrapper.getMsg();
            System.out.println("收到:" + echoMsg);

            String host = ctx.channel().remoteAddress().toString();

            EchoMsg responseMsg = new EchoMsg(echoMsg.getPayload());
            MessageSender.sendMessage(responseMsg, NettyConnection.BizKeyType.ACCOUNT, host, new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {
                    System.out.println("echo over：" + isSuccess);
                }
            });
            super.channelRead(ctx, messageWrapper);
        }
    }


    public static void main(String[] args) {

        EchoServer7 server = new EchoServer7(7222);
        server.start();
        NettySupport.waitInLoop(true);


    }
}

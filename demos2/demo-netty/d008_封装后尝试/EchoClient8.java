package com.danger.demo.d008_封装后尝试;

import com.danger.demo.d007_封装.sender.MessageSender;
import com.danger.demo.d008_封装后尝试.pojo.EchoDecoder;
import com.danger.demo.d008_封装后尝试.pojo.EchoEncoder;
import com.danger.demo.d008_封装后尝试.pojo.EchoProtocol;
import com.danger.demo.d009_1.EchoClientHandler;
import com.danger.demo.d009_1.FixedLengthFrameEncoder;
import com.danger.netty.BaseClient;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import com.danger.netty.handler.ClientConnectChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EchoClient8 extends BaseClient {

    public static void main(String[] args) {

        EchoClient8 client = new EchoClient8("localhost", 7222);
        client.start(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

//                // 进行长度字段解码，这里也会对数据进行粘包和拆包处理
////                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
//                ch.pipeline().addLast(new EchoDecoder());
//                ch.pipeline().addLast(new EchoClientHandler());
//                ch.pipeline().addLast(new EchoEncoder());
//                // LengthFieldPrepender是一个编码器，主要是在响应字节数据前面添加字节长度字段
////                ch.pipeline().addLast(new LengthFieldPrepender(2));
//                ch.pipeline().addLast(new EchoOutboundHandler());

                // 对服务端发送的消息进行粘包和拆包处理，由于服务端发送的消息已经进行了空格补全，
                // 并且长度为20，因而这里指定的长度也为20
                ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                // 将粘包和拆包处理得到的消息转换为字符串
                ch.pipeline().addLast(new StringDecoder());
                // 对客户端发送的消息进行空格补全，保证其长度为20
                ch.pipeline().addLast(new FixedLengthFrameEncoder(20));
                // 客户端发送消息给服务端，并且处理服务端响应的消息
                ch.pipeline().addLast(new com.danger.demo.d009_1.EchoClientHandler());
                ch.pipeline().addLast(new ClientConnectChannelHandler(client.serverName));

            }
        });

        while (true){
            NettySupport.waitInLoop(4, false);
            String s = "client to server";

//            EchoProtocol msg = new EchoProtocol();
//            msg.setLength(s.length());
//            msg.setContent(s);
//
//            ByteBuf msg2 = Unpooled.buffer(4 + msg.getLength());
//            msg2.writeInt(msg.getLength());
//            msg2.writeBytes(msg.getContent().getBytes(StandardCharsets.UTF_8));

            System.out.println("准备发送...");
            MessageSender.sendMessage(s, NettyConnection.BizKeyType.ACCOUNT, client.serverName, new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {

                }
            });
        }


    }


    private String serverName;

    public EchoClient8(String host, int port) {
        super( host, port);
        serverName = "server-for-" + this.hashCode() + "-" + System.currentTimeMillis();
    }

    @Override
    protected   List<ChannelHandler> getHandlers(){
        List<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new ClientConnectChannelHandler(serverName));
//        handlers.add(new EchoInboundHandler3());

        handlers.add(new EchoDecoder());
        handlers.add(new EchoClientHandler());
        handlers.add(new EchoEncoder());
//        handlers.add(new EchoOutboundHandler());
        handlers.add(new EchoOutboundHandler());
        return handlers;
    }

    @ChannelHandler.Sharable
    public static class EchoClientHandler extends BaseInboundHandler{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            EchoProtocol body = (EchoProtocol)msg;
            log("收到：" + body.getContent());

            //forward
//            super.channelRead(ctx, body);
        }
    }




}

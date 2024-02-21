package com.danger.demo.d008_封装后尝试;

import com.danger.demo.d007_封装.sender.MessageSender;
import com.danger.demo.d008_封装后尝试.pojo.EchoDecoder;
import com.danger.demo.d008_封装后尝试.pojo.EchoEncoder;
import com.danger.demo.d008_封装后尝试.pojo.EchoProtocol;
import com.danger.demo.d009_1.EchoServerHandler;
import com.danger.demo.d009_1.FixedLengthFrameEncoder;
import com.danger.netty.BaseServer;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import com.danger.netty.handler.BaseInboundHandler;
import com.danger.netty.handler.ServerConnectManagerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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

public class EchoServer8 extends BaseServer {

    public static void main(String[] args) {

        EchoServer8 server = new EchoServer8(7222);
        server.start(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

//                // 这里将LengthFieldBasedFrameDecoder添加到pipeline的首位，因为其需要对接收到的数据
//                // 进行长度字段解码，这里也会对数据进行粘包和拆包处理
////                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
//                ch.pipeline().addLast(new EchoDecoder());
//                ch.pipeline().addLast(new EchoServerHandler());
//                // LengthFieldPrepender是一个编码器，主要是在响应字节数据前面添加字节长度字段
////                ch.pipeline().addLast(new LengthFieldPrepender(2));
//                ch.pipeline().addLast(new EchoEncoder());
//                ch.pipeline().addLast(new EchoOutboundHandler());

                // 这里将FixedLengthFrameDecoder添加到pipeline中，指定长度为20
                ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                // 将前一步解码得到的数据转码为字符串
                ch.pipeline().addLast(new StringDecoder());
                // 这里FixedLengthFrameEncoder是我们自定义的，用于将长度不足20的消息进行补全空格
                ch.pipeline().addLast(new FixedLengthFrameEncoder(20));
                // 最终的数据处理
                ch.pipeline().addLast(new com.danger.demo.d009_1.EchoServerHandler());

                ch.pipeline().addLast(new ServerConnectManagerHandler());

            }
        });
        NettySupport.waitInLoop(true);


    }

    public EchoServer8(int port) {
        super(port);
    }

    @Override
    protected List<ChannelHandler> getHandlers() {
        List<ChannelHandler> handlers = new ArrayList<>();
//        handlers.add(new ServerConnectManagerHandler());
//        handlers.add(new EchoDecoder());
//        handlers.add(new EchoServerHandler());
//        handlers.add(new EchoEncoder());
//        handlers.add(new EchoOutboundHandler());
        return handlers;
    }

    @ChannelHandler.Sharable
    public static class EchoServerHandler extends BaseInboundHandler {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object in) throws Exception {
//            EchoProtocol body = (EchoProtocol) in;
//            log("收到：" + body.getContent());
            log("收到：" + in);

            // send to client
//            ByteBuf retBody = NettySupport.getByteBuf(body.getContent());

//            EchoProtocol msg = new EchoProtocol();
//            msg.setLength(body.getContent().length());
//            msg.setContent(body.getContent());
//
//
//            ByteBuf msg2 = Unpooled.buffer(4 + msg.getLength());
//            msg2.writeInt(msg.getLength());
//            msg2.writeBytes(msg.getContent().getBytes(StandardCharsets.UTF_8));


            String host = ctx.channel().remoteAddress().toString();
            MessageSender.sendMessage(in, NettyConnection.BizKeyType.DEFAULT, host, new NettySupport.OperationCallback() {
                @Override
                public void onFinish(boolean isSuccess, Object data, String error, Throwable e) {

                }
            });

            //forward
//            super.channelRead(ctx, body);
        }
    }



}

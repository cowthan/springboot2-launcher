package com.danger.demo.d009_1;

import com.danger.netty.NettySupport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 类功能描述
 */
public class EchoClient9 {

    public void connect(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 对服务端发送的消息进行粘包和拆包处理，由于服务端发送的消息已经进行了空格补全，
                            // 并且长度为20，因而这里指定的长度也为20
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                            // 将粘包和拆包处理得到的消息转换为字符串
                            ch.pipeline().addLast(new StringDecoder());
                            // 对客户端发送的消息进行空格补全，保证其长度为20
                            ch.pipeline().addLast(new FixedLengthFrameEncoder(20));
                            // 客户端发送消息给服务端，并且处理服务端响应的消息
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
//            future.channel().closeFuture().sync();
        } finally {
            //group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient9().connect("127.0.0.1", 7222);

        NettySupport.waitInLoop(false);
    }
}

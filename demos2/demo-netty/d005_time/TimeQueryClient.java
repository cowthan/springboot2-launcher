package com.danger.demo.d005_time;

import com.danger.demo.BaseClientChannelHandler;
import com.danger.netty.NettySupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TimeQueryClient {
    private String host;
    private int port;

    public TimeQueryClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getTime() {
        final List<String> ret = new ArrayList<>();
        NettySupport.startClient(true, host, port, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new BaseClientChannelHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        super.channelRead(ctx, msg);
                        String response = NettySupport.getString((ByteBuf) msg);
                        ret.add(response);
                        ctx.close();
                    }
                });
            }
        });
        return ret.get(0);
    }

    public String getTime2() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                final List<String> ret = new ArrayList<>();
                NettySupport.startClient(true, host, port, new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new BaseClientChannelHandler() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                super.channelRead(ctx, msg);
                                String response = NettySupport.getString((ByteBuf) msg);
                                ret.add(response);
                                ctx.close();
                            }
                        });
                    }
                });
                return ret.get(0);
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

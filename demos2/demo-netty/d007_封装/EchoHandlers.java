package com.danger.demo.d007_封装;

import com.danger.demo.d007_封装.client.EchoClient7;
import com.danger.demo.d007_封装.codec.EchoDecoder;
import com.danger.demo.d007_封装.codec.EchoEncoder;
import com.danger.demo.d007_封装.server.EchoServer7;
import com.danger.demo.d007_封装.wrapper.MessageInWrapperHandler;
import com.danger.demo.d007_封装.wrapper.MessageOutWrapperHandler;
import com.danger.netty.handler.ClientConnectChannelHandler;
import com.danger.netty.handler.LogInChannelHandler;
import com.danger.netty.handler.LogOutChannelHandler;
import com.danger.netty.handler.ServerConnectManagerHandler;
import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

public class EchoHandlers {
    public static List<ChannelHandler> getServerHandlers(){
        List<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new ServerConnectManagerHandler());
        handlers.add(new MessageInWrapperHandler());  // ByteBuf to MessageWrapper-ByteBuf
//        handlers.add(new LogInChannelHandler());
//        handlers.add(new LogOutChannelHandler());
        handlers.add(new EchoDecoder());  // inbound MessageWrapper-EchoMsg to MessageWrapper-ByteBuf
        handlers.add(new EchoServer7.EchoServerHandler());
        handlers.add(new EchoEncoder());
        handlers.add(new MessageOutWrapperHandler());
        return handlers;
    }

    public static List<ChannelHandler> getClientHandlers(String serverName){
        List<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new ClientConnectChannelHandler(serverName));
        handlers.add(new MessageInWrapperHandler());
        handlers.add(new EchoDecoder());
//        handlers.add(new LogInChannelHandler());
//        handlers.add(new LogOutChannelHandler());
        handlers.add(new EchoClient7.EchoClientHandler());
        handlers.add(new EchoEncoder());

        handlers.add(new MessageOutWrapperHandler());
        return handlers;
    }
}

package com.danger.demo.d007_封装.sender;

import com.danger.netty.ConnectionManager;
import com.danger.netty.NettyConnection;
import com.danger.netty.NettySupport;
import com.danger.netty.exception.ConnectNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageSender {

    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public static void sendMessage(Object msg, NettyConnection.BizKeyType type, String key, NettySupport.OperationCallback callback) {
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        List<NettyConnection> connections = ConnectionManager.getDefault().findByBusinessKey(type, key);
        if (connections == null || connections.size() == 0) {
            String s = "connection not found for " + type.name() + ":" + key;
            Exception e = new ConnectNotFoundException(s);
            callback.onFinish(false, null, s, e);
        }else{
            for (NettyConnection connection : connections) {
                try {
                    NettySupport.sendTo(connection.getChannel(), msg, callback);
                }catch (Exception e){
                    callback.onFinish(false, null, e.getMessage(), e);
                }
            }
        }

    }

}

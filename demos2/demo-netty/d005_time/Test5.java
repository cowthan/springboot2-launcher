package com.danger.demo.d005_time;

import com.danger.netty.NettySupport;

public class Test5 {
    public static void main(String[] args) {
        NettySupport.setDebug(false);
        TimerServer5.start(7222);

        TimeQueryClient timeQueryClient = new TimeQueryClient("localhost", 7222);
        while (true){
            NettySupport.waitInLoop(3, true);
            System.out.println(">>>>>> get time from server: " + timeQueryClient.getTime());
            System.out.println(">>>>>> get time from server2: " + timeQueryClient.getTime2());
            System.out.println("\n");
        }
    }
}

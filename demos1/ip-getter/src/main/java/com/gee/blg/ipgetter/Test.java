package com.gee.blg.ipgetter;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        String host = Inet4Address.getLocalHost().getHostAddress();
        System.out.println(host); // 只能得到eth序号最大的网卡

        /*
        网卡：eth1, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：Intel(R) Ethernet Connection (11) I219-LM
        10.139.106.41，false，false，false，false
        网卡：eth5, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：VirtualBox Host-Only Ethernet Adapter
        192.168.56.1，false，false，false，false
        网卡：eth11, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：Hyper-V Virtual Ethernet Adapter
        172.28.16.1，false，false，false，false
         */
        NetworkInterface.networkInterfaces().forEach(e->{
            try {
                if(e.isUp() && !e.isLoopback()){
                    System.out.println("网卡：" + e.getName()
                            + ", 是否虚拟：" + e.isVirtual()
                            + ", 是否Loopback：" + e.isLoopback()
                            + ", 是否up：" + e.isUp()
                            + ", 网卡名字：" + e.getDisplayName()
                    );
                    e.inetAddresses().forEach(f->{
                        if(f.getHostAddress().contains(".")){
                            System.out.println(f.getHostAddress() +
                                    "，" + f.isAnyLocalAddress() +
                                    "，" + f.isLinkLocalAddress() +
                                    "，" + f.isLoopbackAddress() +
                                    "，" + f.isMCGlobal()
                                    );
                        }
                    });
                }
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }

        });
    }
}

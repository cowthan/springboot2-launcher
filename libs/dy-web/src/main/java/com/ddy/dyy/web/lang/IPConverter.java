package com.ddy.dyy.web.lang;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 获取 ip 地址
 */
public class IPConverter extends ClassicConverter {

    private String ip;

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        try {
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            return ip;
        } catch (UnknownHostException e) {
            // ignore
        }
        return null;
    }

}

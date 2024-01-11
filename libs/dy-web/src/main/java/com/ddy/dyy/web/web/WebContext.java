package com.ddy.dyy.web.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddy.dyy.web.lang.Lang;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class WebContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static String printServerInfo(ApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        String ip = getLocalIp();
        String port = env.getProperty("server.port");
        if (port == null) {
            port = "8080";
        }

        String path = "";
        log.info("\n----------------------------------------------------------\n\t"
                + "Application started，You can visit:\n\t"
                + "External: \thttp://" + ip + ":" + port + path + "/\n\t"
                + "Local: \t\thttp://localhost:" + port + path + "/\n\t"
                + "----------------------------------------------------------");
        return "http://localhost:" + port;
    }

    public static String getLocalIp() {
        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException var2) {
            throw new RuntimeException(var2);
        }
    }

    /**
     * 获取当前Request
     *
     * @return 结果
     */
    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }



}

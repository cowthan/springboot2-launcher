package com.ddy.dyy.web.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
        String port = env.getProperty("server.port");
        if (port == null) {
            port = "8080";
        }

        String info = "";
        info += "\n----------------------------------------------------------\n\t";
        info += "App running at:\n";
        info += "- Local: http://localhost:" + port + "\n";
        List<Lang.NetworkEth> networkEths = Lang.getIp();
        for (Lang.NetworkEth eth : networkEths) {
            info += String.format("- %s(%s): http://%s:%s/\n", eth.getName(), eth.getDisplayName(), eth.getHost(), port);
        }
        info += "----------------------------------------------------------";
        log.info(info);
        return "http://localhost:" + port;
    }


    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }


}

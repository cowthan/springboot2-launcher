package com.danger.t7;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 *
 */
@Slf4j
@SpringBootApplication
public class T7StarterApplication {

    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ConfigurableApplicationContext application = SpringApplication.run(T7StarterApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = "";
        log.info("\n----------------------------------------------------------\n\t"
                + "Application SysApplication is running! Access URLs:\n\t"
                + "Local: \t\thttp://localhost:" + port + path + "/\n\t"
                + "External: \thttp://" + ip + ":" + port + path + "/\n\t"
                + "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t"
                + "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n"
                + "----------------------------------------------------------");
    }
}

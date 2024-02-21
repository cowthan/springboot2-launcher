package com.geesatcom.blog.bootstrap.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mysql")
@Getter @Setter
public class MySQLProperties  {

    private String host;
    private Integer port;
    private String username;
    private String password;


    private static volatile boolean firstTime = true;
//    @Override
    public void afterPropertiesSet() throws Exception {
//        if(firstTime){
//            host = "xxxxxxxx";
//            firstTime = false;
//        }
    }
}
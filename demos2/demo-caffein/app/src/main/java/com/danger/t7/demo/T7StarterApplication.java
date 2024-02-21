package com.danger.t7.demo;

import com.danger.t7.demo.utils.Lang;
import lib.xx.XXUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 *
 */
@Slf4j
@SpringBootApplication
public class T7StarterApplication {

    public static void main(String[] args) {
        ApplicationContext application = SpringApplication.run(T7StarterApplication.class, args);
        Lang.printServerInfo(application);
        XXUtils.test();
    }
}

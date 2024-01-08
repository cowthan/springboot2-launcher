package com.ddy.demo;

import com.ddy.dyy.web.web.WebContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * starter
 */
@Slf4j
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class DemoStarter {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(DemoStarter.class)
                        .bannerMode(Banner.Mode.OFF)
                        .run(args);
        WebContext.printServerInfo(context);
    }
}

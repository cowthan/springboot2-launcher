package com.example.demo;

import com.example.demo.test.SpringAsync.AsyncBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
//@EnableAsync
//@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        log.info("启动成功：http://localhost:9999/api/test/page?direction=IN&time2=2022-02-22%2022:22:22");

        AsyncBizService asyncBizService = context.getBean(AsyncBizService.class);
        for (int i = 0; i < 5; i++) {
            asyncBizService.testAsync();
        }
    }
}

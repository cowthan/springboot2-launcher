package com.example.demo.test.SpringAsync;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.demo.test.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 此配置用于定义@Async的非默认线程池，即通过@Async("myThreadPool")指定的线程池
 */
@Slf4j
@Configuration
public class AsyncThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor myThreadPool() {
        return new ThreadPoolExecutor(4,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new NamedThreadFactory("async-pool"),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}

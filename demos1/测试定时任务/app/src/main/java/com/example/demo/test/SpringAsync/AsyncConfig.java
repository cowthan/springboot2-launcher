package com.example.demo.test.SpringAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.demo.test.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * 此配置用于代替@Async的默认线程池，只能定义一个
 */
@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 代替@Async的默认线程池
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return new ThreadPoolExecutor(4,
                5,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new NamedThreadFactory("asyn2023"),
//                new ThreadPoolExecutor.CallerRunsPolicy()
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            //可以做一些其他的业务处理，比如：异常记录日志
            log.error("class: {}", method.getDeclaringClass().getName());
            log.error("method：{}", method.getName());
            log.error("type：{}", throwable.getClass().getName());
            log.error("exception：{}", throwable.getMessage());
        };
    }
}
package com.example.demo.test.SpringSchedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 此配置用于定义@Scheduled线程池，以取代@Scheduled的默认单线程，但任务还是串行模式！！！切记!
 * 想要并行，就需要配合@Async
 */
@Slf4j
@Configuration
public class ScheduleThreadPoolConfig {
//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(10);
//        scheduler.initialize();
//        return scheduler;
//    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("my-task-executor-");
        return executor;
    }
}

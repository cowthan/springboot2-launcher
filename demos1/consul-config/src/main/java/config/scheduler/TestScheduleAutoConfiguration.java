package com.geesatcom.blog.bootstrap.scheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
public class TestScheduleAutoConfiguration {
    public static final String CONFIG_WATCH_TASK_SCHEDULER_NAME2 = "configWatchTaskScheduler2222";

    @Bean(name = CONFIG_WATCH_TASK_SCHEDULER_NAME2)
    public TaskScheduler configWatchTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}

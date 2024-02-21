package com.example.demo.test.quartz;

import com.example.demo.test.Tools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/*
1 默认是严格的按照固定间隔执行，即并行；默认线程池可能是10个线程
 */
@Component
@Slf4j
public class QuartzJob2 {
    public void doTask(String s, Integer a) throws JobExecutionException {
        log.info("执行quartz QuartzJob2：{} - {}, {}", Tools.getNow(), s, a);
    }
}

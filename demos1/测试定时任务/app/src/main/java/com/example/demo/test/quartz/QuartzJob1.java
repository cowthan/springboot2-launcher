package com.example.demo.test.quartz;

import com.example.demo.test.Tools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
1 默认是严格的按照固定间隔执行，即并行；默认线程池可能是10个线程
 */
@Slf4j
public class QuartzJob1 implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("执行quartz QuartzJob1：" + Tools.getNow());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            log.error("", e);
//        }
    }
}

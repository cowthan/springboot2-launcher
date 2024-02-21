package com.example.demo.test.SpringSchedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.test.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * scheduling默认单线程，线程名为scheduling-1，即串行执行，碰到耗时任务，则下次任务启动时间会受影响，不同调度方式影响不同，
 * 使用@Async注解，则串行变并行，间隔时间就比较固定，但本次任务执行时还需要考虑上次任务是否执行完毕，以避免并行运行对数据造成破坏
 */
@Slf4j
@Component
public class ScheduledBizService {
    @Scheduled(cron = "0/3 * * * * ?") //每3秒执行一次，计时是从上次开始时间开始，但上次没执行完则本次会跳过（结束后还会等到当前间隔时间到），启动完成之后才开始执行
    public void byCorn() {
        log.info("定时任务开始 ByCorn：" + Tools.getNow());
        doTask();
        log.info("定时任务结束 ByCorn：" + Tools.getNow());
    }

//    @Scheduled(fixedRate = 3000) //每3秒执行一次，串行，计时是从上次开始时间开始，但要等上次任务执行完（结束后会立即开始下一次），启动完成之前就开始执行
//    @Async // @Async让调度任务以并行方式执行，并可以指定线程池
//    public void byFixedRate() {
//        log.info("定时任务开始 ByFixedRate：" + getNow());
//        doTask();
//        log.info("定时任务结束 ByFixedRate：" + getNow());
//    }

//    @Scheduled(fixedDelay = 3000) //这个也是重复执行，串行，计时是从上次结束时间开始，启动完成之前就开始执行
//    public void byFixedDelay() {
//        log.info("定时任务开始 ByFixedDelay：" + getNow());
//        doTask();
//        log.info("定时任务结束 ByFixedDelay：" + getNow());
//    }

    private void doTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }


}

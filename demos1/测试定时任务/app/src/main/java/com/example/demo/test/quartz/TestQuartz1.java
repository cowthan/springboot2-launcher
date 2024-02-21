package com.example.demo.test.quartz;

import java.util.List;
import java.util.Properties;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.ResourceLoaderClassLoadHelper;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class TestQuartz1 {
    public static void main(String[] args) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.INFO);
        });
        try {
            // Grab the Scheduler instance from the Factory
//            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            Scheduler scheduler = getSchedulerFactory().getScheduler();

            // and start it off
            scheduler.start();

            // 定义任务
            // define the job and tie it to our HelloJob class
            Class<? extends Job> jobClass = QuartzJob1.class;
            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobClass.getName(), jobClass.getName())
                    .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(3)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);

            //your application will not terminate until you call scheduler.shutdown(), because there will be active threads.
//            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    private static StdSchedulerFactory getSchedulerFactory() throws SchedulerException {
        Properties prop = new Properties();
        // quartz参数
        prop.put("org.quartz.scheduler.instanceName", "schedule名字");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
//        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        // 线程池配置
        prop.put("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
        prop.put("org.quartz.threadPool.threadCount", "5");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        // JobStore配置
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//        prop.put("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
        // prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");// sqlserver 启用
        prop.put("org.quartz.jobStore.class", RAMJobStore.class.getName());
        // 集群配置
//        prop.put("org.quartz.jobStore.isClustered", "true");
//        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
//        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
//        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");

        prop.put("org.quartz.scheduler.classLoadHelper.class", ResourceLoaderClassLoadHelper.class.getName());

        StdSchedulerFactory factory = new StdSchedulerFactory(prop);
//        factory.setDataSource(dataSource);
//        factory.setSchedulerName("RuoyiScheduler");
//        factory.setStartupDelay(1); // 延时启动
//        factory.setOverwriteExistingJobs(true); // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
//        factory.setAutoStartup(true);// 设置自动启动，默认为true

        return factory;
    }
}

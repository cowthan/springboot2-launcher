package com.example.demo.quartz;

import java.util.List;

import com.example.demo.quartz.db.ScheduledJobStore;
import com.example.demo.quartz.models.ScheduledJob;
import com.example.demo.quartz.util.ScheduleConstants;
import com.example.demo.quartz.util.ScheduleUtils;
import com.example.demo.utils.TaskException;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class SpringQuartz {


    public static void initJobs(Scheduler scheduler, ScheduledJobStore store) throws SchedulerException, TaskException {
        scheduler.clear();
        List<ScheduledJob> jobList = store.findAll();
        if (jobList != null) {
            for (ScheduledJob job : jobList) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }
        }
    }

    public static void addJob(Scheduler scheduler, ScheduledJobStore store, ScheduledJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        boolean ok = store.create(job);
        if (ok) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    public static void updateJob(Scheduler scheduler, ScheduledJobStore store, ScheduledJob newJob) throws SchedulerException, TaskException {
        Long jobId = newJob.getJobId();
        ScheduledJob oldJob = store.findById(newJob.getJobId());
        if (oldJob == null) {
            throw new RuntimeException("Job不存在-" + newJob.getJobId());
        }
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, oldJob.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            boolean deleted = store.delete(jobId);
            if (deleted) {
                scheduler.deleteJob(jobKey);
            }
        }
        boolean ok = store.create(newJob);
        if (ok) {
            ScheduleUtils.createScheduleJob(scheduler, newJob);
        }
    }

    public static void pauseJob(Scheduler scheduler, ScheduledJobStore store, ScheduledJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        boolean updated = store.updateJobStatus(jobId, job.getStatus());
        if (updated) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    public static void resumeJob(Scheduler scheduler, ScheduledJobStore store, ScheduledJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        boolean updated = store.updateJobStatus(jobId, job.getStatus());
        if (updated) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }

    }

    public static void deleteJob(Scheduler scheduler, ScheduledJobStore store, ScheduledJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        boolean deleted = store.delete(jobId);
        if (deleted) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }

    }

    public static void runJobNow(Scheduler scheduler, ScheduledJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
//        SysJob properties = selectJobById(job.getJobId());
        ScheduledJob properties = job;
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey, dataMap);
        } else {
            throw new RuntimeException("没有找到该任务：" + job.getInvokeTarget());
        }
    }
}

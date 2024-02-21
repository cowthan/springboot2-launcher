package com.example.demo.quartz.db;

import java.util.List;

import com.example.demo.quartz.models.ScheduledJob;
import com.example.demo.quartz.models.ScheduledJobLog;


public interface ScheduledJobStore {
    List<ScheduledJob> findAll();

    boolean create(ScheduledJob job);

    ScheduledJob findById(long jobId);

    boolean delete(long jobId);

    boolean updateJobStatus(long jobId, String status);

    void addJobLog(ScheduledJobLog sysJobLog);
}

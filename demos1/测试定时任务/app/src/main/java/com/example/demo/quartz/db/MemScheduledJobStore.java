package com.example.demo.quartz.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.quartz.models.ScheduledJob;
import com.example.demo.quartz.models.ScheduledJobLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemScheduledJobStore implements ScheduledJobStore {

    private ConcurrentHashMap<Long, ScheduledJob> jobs = new ConcurrentHashMap<>();

    @Override
    public List<ScheduledJob> findAll() {
        List<ScheduledJob> ret = new ArrayList<>();
        ret.addAll(jobs.values());
        return ret;
    }

    @Override
    public boolean create(ScheduledJob job) {
        jobs.put(job.getJobId(), job);
        return true;
    }

    @Override
    public ScheduledJob findById(long jobId) {
        return jobs.getOrDefault(jobId, null);
    }

    @Override
    public boolean delete(long jobId) {
        ScheduledJob removed = jobs.remove(jobId);
        return removed != null;
    }

    @Override
    public boolean updateJobStatus(long jobId, String status) {
        ScheduledJob job = jobs.getOrDefault(jobId, null);
        if (job != null && !status.equals(job.getStatus())) {
            job.setStatus(status);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addJobLog(ScheduledJobLog sysJobLog) {
        log.info("任务执行日志：{}", sysJobLog);
    }
}

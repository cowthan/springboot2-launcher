package com.example.demo.quartz.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.quartz.models.ScheduledJob;
import com.example.demo.quartz.models.ScheduledJobLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JdbcScheduledJobStore implements ScheduledJobStore {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private RowMapper<ScheduledJob> rowMapper = new RowMapper<ScheduledJob>() {
        @Override
        public ScheduledJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScheduledJob job = new ScheduledJob();
            job.setJobId(rs.getLong("job_id"));
            job.setJobName(rs.getString("job_name"));
            job.setJobGroup(rs.getString("job_group"));
            job.setInvokeTarget(rs.getString("invoke_target"));
            job.setCronExpression(rs.getString("cron_expression"));
            job.setMisfirePolicy(rs.getString("misfire_policy"));
            job.setConcurrent(rs.getString("concurrent"));
            job.setStatus(rs.getString("status"));
            return job;
        }
    };

    @Override
    public List<ScheduledJob> findAll() {
        List<ScheduledJob> queried = jdbcTemplate.query("select * from t_job", rowMapper);
        return queried;
    }

    @Override
    public boolean create(ScheduledJob job) {
        Map<String, Object> map = new HashMap<>();
        map.put("job_name", job.getJobName());
        map.put("job_group", job.getJobGroup());
        map.put("invoke_target", job.getInvokeTarget());
        map.put("cron_expression", job.getCronExpression());
        map.put("misfire_policy", job.getMisfirePolicy());
        map.put("concurrent", job.getConcurrent());
        map.put("status", job.getStatus());
        int updated = jdbcTemplate.update("insert into t_job(job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status) " +
                "values(:job_name, :job_group, :invoke_target, :cron_expression, :misfire_policy, :concurrent, :status)", map);
        return updated == 1;
    }

    @Override
    public ScheduledJob findById(long jobId) {
        List<ScheduledJob> queried = jdbcTemplate.query("select * from t_job where job_id = " + jobId, rowMapper);
        return queried.size() > 0 ? queried.get(0) : null;
    }

    @Override
    public boolean delete(long jobId) {
        int updated = jdbcTemplate.update("delete from t_job where job_id = " + jobId, new HashMap<>());
        return updated == 1;
    }

    @Override
    public boolean updateJobStatus(long jobId, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("job_Id", jobId);
        int updated = jdbcTemplate.update("update t_job set status = :status where job_id = :job_id and status != :status", map);
        return updated == 1;
    }

    @Override
    public void addJobLog(ScheduledJobLog sysJobLog) {
        log.info("任务执行日志：{}", sysJobLog);
    }
}

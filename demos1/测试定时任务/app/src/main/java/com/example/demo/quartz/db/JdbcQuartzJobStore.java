package com.example.demo.quartz.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobPersistenceException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.JobStore;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.spi.TriggerFiredResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JdbcQuartzJobStore implements JobStore {

    protected SchedulerSignaler signaler;
    private volatile boolean schedulerRunning = false;
    private volatile boolean shutdown = false;
    private boolean isClustered = false;


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        this.signaler = signaler;
        log.info("JdbcQuartzJobStore initialized.");
    }

    @Override
    public void schedulerStarted() throws SchedulerException {
        // nothing to do
    }

    @Override
    public void schedulerPaused() {
        // nothing to do
        schedulerRunning = false;
    }

    @Override
    public void schedulerResumed() {
        // nothing to do
        schedulerRunning = true;
    }

    @Override
    public void shutdown() {
        // nothing to do
        shutdown = true;
    }

    @Override
    public boolean supportsPersistence() {
        return true;
    }

    @Override
    public long getEstimatedTimeToReleaseAndAcquireTrigger() {
        return 70;
    }

    @Override
    public boolean isClustered() {
        return isClustered;
    }

    @Override
    public void storeJobAndTrigger(JobDetail newJob, OperableTrigger newTrigger) throws ObjectAlreadyExistsException, JobPersistenceException {
        storeJob(newJob, false);
        storeTrigger(newTrigger, false);
    }

    @Override
    public void storeJob(JobDetail newJob, boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {
//        String INSERT_JOB_DETAIL = "INSERT INTO "
//                + TABLE_PREFIX_SUBST + TABLE_JOB_DETAILS + " ("
//                + COL_SCHEDULER_NAME + ", " + COL_JOB_NAME
//                + ", " + COL_JOB_GROUP + ", " + COL_DESCRIPTION + ", "
//                + COL_JOB_CLASS + ", " + COL_IS_DURABLE + ", "
//                + COL_IS_NONCONCURRENT +  ", " + COL_IS_UPDATE_DATA + ", "
//                + COL_REQUESTS_RECOVERY + ", "
//                + COL_JOB_DATAMAP + ") " + " VALUES(" + SCHED_NAME_SUBST + ", ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    }

    @Override
    public void storeJobsAndTriggers(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) throws ObjectAlreadyExistsException, JobPersistenceException {

    }

    @Override
    public boolean removeJob(JobKey jobKey) throws JobPersistenceException {
        return false;
    }

    @Override
    public boolean removeJobs(List<JobKey> jobKeys) throws JobPersistenceException {
        return false;
    }

    @Override
    public JobDetail retrieveJob(JobKey jobKey) throws JobPersistenceException {
        return null;
    }

    @Override
    public void storeTrigger(OperableTrigger newTrigger, boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {

    }

    @Override
    public boolean removeTrigger(TriggerKey triggerKey) throws JobPersistenceException {
        return false;
    }

    @Override
    public boolean removeTriggers(List<TriggerKey> triggerKeys) throws JobPersistenceException {
        return false;
    }

    @Override
    public boolean replaceTrigger(TriggerKey triggerKey, OperableTrigger newTrigger) throws JobPersistenceException {
        return false;
    }

    @Override
    public OperableTrigger retrieveTrigger(TriggerKey triggerKey) throws JobPersistenceException {
        return null;
    }

    @Override
    public boolean checkExists(JobKey jobKey) throws JobPersistenceException {
        return false;
    }

    @Override
    public boolean checkExists(TriggerKey triggerKey) throws JobPersistenceException {
        return false;
    }

    @Override
    public void clearAllSchedulingData() throws JobPersistenceException {

    }

    @Override
    public void storeCalendar(String name, Calendar calendar, boolean replaceExisting, boolean updateTriggers) throws ObjectAlreadyExistsException, JobPersistenceException {

    }

    @Override
    public boolean removeCalendar(String calName) throws JobPersistenceException {
        return false;
    }

    @Override
    public Calendar retrieveCalendar(String calName) throws JobPersistenceException {
        return null;
    }

    @Override
    public int getNumberOfJobs() throws JobPersistenceException {
        return 0;
    }

    @Override
    public int getNumberOfTriggers() throws JobPersistenceException {
        return 0;
    }

    @Override
    public int getNumberOfCalendars() throws JobPersistenceException {
        return 0;
    }

    @Override
    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public List<String> getJobGroupNames() throws JobPersistenceException {
        return null;
    }

    @Override
    public List<String> getTriggerGroupNames() throws JobPersistenceException {
        return null;
    }

    @Override
    public List<String> getCalendarNames() throws JobPersistenceException {
        return null;
    }

    @Override
    public List<OperableTrigger> getTriggersForJob(JobKey jobKey) throws JobPersistenceException {
        return null;
    }

    @Override
    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws JobPersistenceException {
        return null;
    }

    @Override
    public void resetTriggerFromErrorState(TriggerKey triggerKey) throws JobPersistenceException {

    }

    @Override
    public void pauseTrigger(TriggerKey triggerKey) throws JobPersistenceException {

    }

    @Override
    public Collection<String> pauseTriggers(GroupMatcher<TriggerKey> matcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public void pauseJob(JobKey jobKey) throws JobPersistenceException {

    }

    @Override
    public Collection<String> pauseJobs(GroupMatcher<JobKey> groupMatcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public void resumeTrigger(TriggerKey triggerKey) throws JobPersistenceException {

    }

    @Override
    public Collection<String> resumeTriggers(GroupMatcher<TriggerKey> matcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public Set<String> getPausedTriggerGroups() throws JobPersistenceException {
        return null;
    }

    @Override
    public void resumeJob(JobKey jobKey) throws JobPersistenceException {

    }

    @Override
    public Collection<String> resumeJobs(GroupMatcher<JobKey> matcher) throws JobPersistenceException {
        return null;
    }

    @Override
    public void pauseAll() throws JobPersistenceException {

    }

    @Override
    public void resumeAll() throws JobPersistenceException {

    }

    @Override
    public List<OperableTrigger> acquireNextTriggers(long noLaterThan, int maxCount, long timeWindow) throws JobPersistenceException {
        return null;
    }

    @Override
    public void releaseAcquiredTrigger(OperableTrigger trigger) {

    }

    @Override
    public List<TriggerFiredResult> triggersFired(List<OperableTrigger> triggers) throws JobPersistenceException {
        return null;
    }

    @Override
    public void triggeredJobComplete(OperableTrigger trigger, JobDetail jobDetail, Trigger.CompletedExecutionInstruction triggerInstCode) {

    }

    @Override
    public void setInstanceId(String schedInstId) {

    }

    @Override
    public void setInstanceName(String schedName) {

    }

    @Override
    public void setThreadPoolSize(int poolSize) {

    }

    @Override
    public long getAcquireRetryDelay(int failureCount) {
        return 0;
    }
}

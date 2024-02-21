package com.example.demo.web;

import com.example.demo.BaseController;
import com.example.demo.quartz.SpringQuartz;
import com.example.demo.quartz.models.ScheduledJob;
import com.example.demo.utils.TaskException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/quartz")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuartzController extends BaseController {

    private final Scheduler scheduler;

    /*
insert into sys_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
     */
    @PostMapping("/runTask")
    public Object runTask(@RequestBody ScheduledJob job) {
        try {
//            SpringQuartz.addJob(scheduler, job);
            SpringQuartz.runJobNow(scheduler, job);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


}
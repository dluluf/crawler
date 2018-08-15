package com.pinganfu.crawler.admin.service.impl;

import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.model.TaskBO;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("quartzManager")
public class QuartzManagerImpl implements QuartzManager {
    private final static String JOB_NAME = "com.pinganfu.crawler.scheduler.job.SearchListPageJob";
//    private final static String JOB_NAME = "com.pinganfu.crawler.scheduler.job.XICIDAILIJOB";
    @Autowired
    private Scheduler scheduler;

    public void startJob(TaskBO taskBO) throws ClassNotFoundException{

        Class jobClass = Class.forName(JOB_NAME);
        JobDetail jobDetail = JobBuilder.newJob(jobClass).
                withIdentity(taskBO.getTaskName(), taskBO.getTaskName()).build();

        TriggerBuilder<Trigger> triggerBuilder =   TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(taskBO.getTaskName(), taskBO.getTaskName());
        triggerBuilder.startNow();
        Trigger trigger =null;
        if(taskBO.getCron()==null || "".equals(taskBO.getCron())){
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule());
            trigger = triggerBuilder.build();
        }else{
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(taskBO.getCron()));
            trigger = triggerBuilder.build();
        }
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    public void startJob2(TaskBO taskBO) throws ClassNotFoundException{

        Class jobClass = Class.forName("com.pinganfu.crawler.scheduler.job.XICIDAILIJOB");
        JobDetail jobDetail = JobBuilder.newJob(jobClass).
                withIdentity(taskBO.getTaskName(), taskBO.getTaskName()).build();

        TriggerBuilder<Trigger> triggerBuilder =   TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(taskBO.getTaskName(), taskBO.getTaskName());
        triggerBuilder.startNow();
        Trigger trigger =null;
        if(taskBO.getCron()==null || "".equals(taskBO.getCron())){
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule());
            trigger = triggerBuilder.build();
        }else{
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(taskBO.getCron()));
            trigger = triggerBuilder.build();
        }
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

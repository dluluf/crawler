package com.pinganfu.crawler.admin.service.impl;

import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.model.JobInfo;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("quartzManager")
public class QuartzManagerImpl implements QuartzManager {
    @Autowired
    private Scheduler scheduler;

    public void startJob(JobInfo jobInfo, FetchConfigInfo fetchConfigInfo) throws ClassNotFoundException{
        String jobName = jobInfo.getJobName();
        String jobGroupName = jobInfo.getJobGroupName();
        String triggerName = jobInfo.getTriggerName();
        String triggerGroupName = jobInfo.getTriggerGroupName();
        Class jobClass = Class.forName(jobInfo.getJobClass());

        String cron = jobInfo.getCron();

        JobDataMap dataMap =new JobDataMap();
        dataMap.put("taskId",fetchConfigInfo.getTaskId());
        dataMap.put("taskName",fetchConfigInfo.getTaskName());
        dataMap.put("pageSize",fetchConfigInfo.getPageSize());
        dataMap.put("contentListCssSelector",fetchConfigInfo.getContentListCssSelector());
        dataMap.put("fieldsCssSelector",fetchConfigInfo.getFieldsCssSelector());
        dataMap.put("urlTemplate",fetchConfigInfo.getUrlTemplate());
        dataMap.put("pageParams",fetchConfigInfo.getPageParams());


        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).
                        usingJobData(dataMap).build();

        TriggerBuilder<Trigger> triggerBuilder =   TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        triggerBuilder.startNow();
        if(cron==null || "".equals(cron)){
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule());
            SimpleTrigger trigger = (SimpleTrigger) triggerBuilder.build();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
                if (scheduler.isShutdown()) {
                    scheduler.start();
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }


        }else{
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

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
}

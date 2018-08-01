package com.pinganfu.crawler.admin.service.impl;

import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.model.TaskConfigBO;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("quartzManager")
public class QuartzManagerImpl implements QuartzManager {
    @Autowired
    private Scheduler scheduler;

    public void startJob(TaskConfigBO taskConfigBO) throws ClassNotFoundException{


        Class jobClass = Class.forName(taskConfigBO.getJobClass());

        JobDataMap dataMap =new JobDataMap();
        Map<String,TaskConfigBO> taskConfigInfo = new HashMap<String,TaskConfigBO>();
        taskConfigInfo.put("taskConfigInfo",taskConfigBO);
        dataMap.putAll(taskConfigInfo);
//        dataMap.put("taskId", taskConfigBO.getTaskId());
//        dataMap.put("taskName", taskConfigBO.getTaskName());
//        dataMap.put("pageSize", taskConfigBO.getPageSize());
//        dataMap.put("contentListCssSelector", taskConfigBO.getContentListCssSelector());
//        dataMap.put("fieldsCssSelector", taskConfigBO.getFieldsCssSelector());
//        dataMap.put("urlTemplate", taskConfigBO.getUrlTemplate());
//        dataMap.put("pageParams", taskConfigBO.getPageParams());


        JobDetail jobDetail = JobBuilder.newJob(jobClass).
                withIdentity(taskConfigBO.getTaskName(), taskConfigBO.getTaskName()).usingJobData(dataMap).build();

        TriggerBuilder<Trigger> triggerBuilder =   TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(taskConfigBO.getTaskName(), taskConfigBO.getTaskName());
        triggerBuilder.startNow();
        Trigger trigger =null;
        if(taskConfigBO.getCron()==null || "".equals(taskConfigBO.getCron())){
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule());
            trigger = triggerBuilder.build();
        }else{
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(taskConfigBO.getCron()));
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

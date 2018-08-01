package com.pinganfu.crawler.admin.controller;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.model.TaskConfigDO;
import com.pinganfu.crawler.data.model.JobInfo;
import com.pinganfu.crawler.data.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value="/list")
    public String taskList(Model model){
        List<TaskConfigDO> taskConfigDOList = taskService.getAllTask();
        model.addAttribute("taskList",taskConfigDOList);
        return "task/list";
    }




    @RequestMapping(value="/addTask")
    @ResponseBody
    public String addTask(Model model, TaskConfigDO taskConfigDO){
        taskService.addTask(taskConfigDO);
        return "success";
    }

    @RequestMapping(value="/toAdd")
    public String add(Model model){
        return "task/addTask";
    }

    @RequestMapping(value="/start")
    @ResponseBody
    public String start(Model model, String id){
        TaskConfigDO taskConfigDO = taskService.getTaskById(id);

        JobInfo jobInfo = new JobInfo();
//        jobInfo.setJobClass("com.pinganfu.crawler.scheduler.job.JDSearchPageJob");
        jobInfo.setJobClass("com.pinganfu.crawler.scheduler.job.TmallSearchPageJob");
        jobInfo.setJobGroupName(taskConfigDO.getTaskName());
        jobInfo.setJobName(taskConfigDO.getTaskName());
        jobInfo.setTriggerName(taskConfigDO.getTaskName());
        jobInfo.setTriggerGroupName(taskConfigDO.getTaskName());
        jobInfo.setJobId(taskConfigDO.getId());
        jobInfo.setCron(taskConfigDO.getCron());

        FetchConfigInfo fetchConfigInfo = new FetchConfigInfo();
        fetchConfigInfo.setContentListCssSelector(taskConfigDO.getContentListCssSelector());
        fetchConfigInfo.setPageSize(Integer.valueOf(taskConfigDO.getPageSize()));

        fetchConfigInfo.setFieldsCssSelector((Map)JSON.parse(taskConfigDO.getFieldsCssSelector()));
        fetchConfigInfo.setTaskId(taskConfigDO.getId());
        fetchConfigInfo.setTaskName(taskConfigDO.getTaskName());
        fetchConfigInfo.setPageParams(taskConfigDO.getPageParams());
        fetchConfigInfo.setUrlTemplate(taskConfigDO.getUrlTemplate());

        try {
            quartzManager.startJob(jobInfo, fetchConfigInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return "success";
    }
}

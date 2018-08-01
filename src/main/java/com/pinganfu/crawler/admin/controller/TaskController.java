package com.pinganfu.crawler.admin.controller;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.model.TaskConfigBO;
import com.pinganfu.crawler.data.model.TaskConfigDO;
import com.pinganfu.crawler.data.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

        TaskConfigBO taskConfigBO = new TaskConfigBO();
        taskConfigBO.setContentListCssSelector(taskConfigDO.getContentListCssSelector());
        taskConfigBO.setFieldsCssSelector((Map)JSON.parse(taskConfigDO.getFieldsCssSelector()));
        taskConfigBO.setTaskId(taskConfigDO.getId());
        taskConfigBO.setTaskName(taskConfigDO.getTaskName());
        taskConfigBO.setCron(taskConfigDO.getCron());
        taskConfigBO.setSeedUrl(taskConfigDO.getSeedUrl());

        taskConfigBO.setJobClass("com.pinganfu.crawler.scheduler.job.TmallSearchPageJob");
//        taskConfigBO.setJobClass("com.pinganfu.crawler.scheduler.job.JDSearchPageJob");
        try {
            quartzManager.startJob(taskConfigBO);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return "success";
    }
}

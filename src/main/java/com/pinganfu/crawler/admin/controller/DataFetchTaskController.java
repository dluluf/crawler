package com.pinganfu.crawler.admin.controller;

import com.pinganfu.crawler.admin.service.QuartzManager;
import com.pinganfu.crawler.data.dao.TaskDao;
import com.pinganfu.crawler.data.dao.UrlConfigDao;
import com.pinganfu.crawler.data.model.TaskBO;
import com.pinganfu.crawler.data.model.TaskDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/fetchTask")
public class DataFetchTaskController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UrlConfigDao urlConfigDao;

    @RequestMapping(value="/addConfig")
    public String addUrlConfig(Model model){
        return "fetchtask/task";
    }

    @RequestMapping(value="/list")
    public String taskList(Model model){
        List<TaskDO> taskDaoList = taskDao.selectAll();
        model.addAttribute("taskList",taskDaoList);
        return "fetchtask/list";
    }

    @RequestMapping(value="/addTask")
    @ResponseBody
    public String addTask(Model model, TaskDO taskDO){
        taskDao.insert(taskDO);
        return "success";
    }

    @RequestMapping(value="/toAdd")
    public String add(Model model){
        return "task/addTask";
    }

    @RequestMapping(value="/start")
    @ResponseBody
    public String start(Model model, String id){
        TaskBO taskBO = new TaskBO();
        taskBO.setTaskName("111111");

        try {
            quartzManager.startJob(taskBO);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return "success";
    }

    @RequestMapping(value="/xici")
    @ResponseBody
    public String xici(Model model, String id){
        TaskBO taskBO = new TaskBO();
        taskBO.setTaskName("xici");

        try {
            quartzManager.startJob2(taskBO);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return "success";
    }
}

package com.pinganfu.crawler.data.service.impl;

import com.pinganfu.crawler.data.dao.TaskConfigDao;
import com.pinganfu.crawler.data.model.TaskConfigDO;
import com.pinganfu.crawler.data.service.TaskService;
import com.pinganfu.crawler.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fetchTaskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private TaskConfigDao taskConfigDao;

    public void addTask(TaskConfigDO taskConfigDO) {
        taskConfigDO.setId(String .valueOf(snowflakeIdWorker.nextId()));
        taskConfigDao.addTaskConfig(taskConfigDO);
    }



    public void start(TaskConfigDO taskConfigDO) {

    }

    public List<TaskConfigDO> getAllTask() {
        List<TaskConfigDO> taskConfigDOList = taskConfigDao.getAllTask();
        return taskConfigDOList;
    }

    @Override
    public TaskConfigDO getTaskById(String id) {
        TaskConfigDO taskConfigDO = taskConfigDao.getTaskById(id);
        return taskConfigDO;
    }


    public void reStartTask(TaskConfigDO taskConfigDO) {

    }

    public void deleteTask(TaskConfigDO taskConfigDO) {

    }

    public void pauseTask(TaskConfigDO taskConfigDO) {

    }
}

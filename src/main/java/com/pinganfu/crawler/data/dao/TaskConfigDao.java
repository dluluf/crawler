package com.pinganfu.crawler.data.dao;

import com.pinganfu.crawler.data.model.TaskConfigDO;

import java.util.List;

public interface TaskConfigDao {
    List<TaskConfigDO> getAllTask();
    void addTaskConfig(TaskConfigDO taskConfigDO);
    TaskConfigDO getTaskById(String id);
}

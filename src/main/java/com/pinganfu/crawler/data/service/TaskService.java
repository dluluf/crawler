package com.pinganfu.crawler.data.service;

import com.pinganfu.crawler.data.model.TaskConfigDO;

import java.util.List;

public interface TaskService {

    public void start(TaskConfigDO taskConfigDO);
    /**
     * 获取所有的任务信息
     * @return 返回数据表中的任务信息
     */
    public List<TaskConfigDO> getAllTask();
    public TaskConfigDO getTaskById(String id);

    /**
     * 添加一个抓取任务
     * @param taskConfigDO 任务信息
     * @return
     */
    public void addTask(TaskConfigDO taskConfigDO);

    public void reStartTask(TaskConfigDO taskConfigDO);

    public void deleteTask(TaskConfigDO taskConfigDO);

    public void pauseTask(TaskConfigDO taskConfigDO);
}

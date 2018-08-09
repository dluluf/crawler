package com.pinganfu.crawler.data.service;

import com.pinganfu.crawler.data.model.TaskDO;

import java.util.List;

public interface TaskService {

    public void start(TaskDO taskDO);
    /**
     * 获取所有的任务信息
     * @return 返回数据表中的任务信息
     */
    public List<TaskDO> getAllTask();
    public TaskDO getTaskById(String id);

    /**
     * 添加一个抓取任务
     * @param taskDO 任务信息
     * @return
     */
    public void addTask(TaskDO taskDO);

}

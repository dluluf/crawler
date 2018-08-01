package com.pinganfu.crawler.admin.service;

import com.pinganfu.crawler.data.model.TaskConfigBO;

public interface QuartzManager {
    void startJob(TaskConfigBO taskConfigBO) throws ClassNotFoundException;
}

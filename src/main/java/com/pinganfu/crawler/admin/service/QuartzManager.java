package com.pinganfu.crawler.admin.service;

import com.pinganfu.crawler.data.model.TaskBO;

public interface QuartzManager {
    void startJob(TaskBO taskBO) throws ClassNotFoundException;
    void startJob2(TaskBO taskBO) throws ClassNotFoundException;
}

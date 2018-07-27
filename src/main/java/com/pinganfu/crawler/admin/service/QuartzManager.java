package com.pinganfu.crawler.admin.service;

import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.model.JobInfo;

public interface QuartzManager {
    void startJob(JobInfo jobInfo, FetchConfigInfo fetchConfigInfo) throws ClassNotFoundException;
}

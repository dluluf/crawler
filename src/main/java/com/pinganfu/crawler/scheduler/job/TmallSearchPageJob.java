package com.pinganfu.crawler.scheduler.job;

import com.pinganfu.crawler.data.model.TaskConfigBO;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.download.SeleniumDownloader;
import com.pinganfu.crawler.fetcher.processor.ListSearchPageProcessor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;


public class TmallSearchPageJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TaskConfigBO taskConfigBO = (TaskConfigBO) jobDataMap.get("taskConfigInfo");
        //设置批次号
        taskConfigBO.setBatchNo(taskConfigBO.getTaskId()+System.currentTimeMillis());
        Spider spider = Spider.create(new ListSearchPageProcessor(taskConfigBO));
        SeleniumDownloader seleniumDownloader =
                new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        spider.setDownloader(seleniumDownloader);
        spider.addUrl(taskConfigBO.getSeedUrl());
        spider.addPipeline(new DataPipeline()).run();
    }
}

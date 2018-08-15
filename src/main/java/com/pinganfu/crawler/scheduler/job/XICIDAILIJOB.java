package com.pinganfu.crawler.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.data.model.UrlConfigDO;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.data.pipeline.XICIDAILIPipeline;
import com.pinganfu.crawler.fetcher.download.PageParamsDownloader;
import com.pinganfu.crawler.fetcher.download.SeleniumDownloader;
import com.pinganfu.crawler.fetcher.processor.SearchListPageProcessor;
import com.pinganfu.crawler.fetcher.processor.XICIPageProcessor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.List;
import java.util.Map;

public class XICIDAILIJOB implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        XICIPageProcessor processor = new XICIPageProcessor();
        Spider spider = Spider.create(processor);
        String baseUrl = "http://www.xicidaili.com/wn/";
        for (int i = 0; i<100; i++){
            String url = baseUrl+i;
            spider.addUrl(url);
        }
        spider.addPipeline(new XICIDAILIPipeline());
        spider.run();
    }
}

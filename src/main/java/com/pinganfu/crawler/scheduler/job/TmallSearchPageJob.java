package com.pinganfu.crawler.scheduler.job;

import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.download.SeleniumDownloader;
import com.pinganfu.crawler.fetcher.processor.TianMaoSearchPageProcessor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;

import java.util.Map;

public class TmallSearchPageJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String taskId = (String)jobDataMap.get("taskId");
        String contentListCssSelector = (String)jobDataMap.get("contentListCssSelector");
        Map fieldsCssSelectorMap = (Map)jobDataMap.get("fieldsCssSelector");

        FetchConfigInfo fetchConfigInfo = new FetchConfigInfo();
        fetchConfigInfo.setBatchNo(taskId+System.currentTimeMillis());
        fetchConfigInfo.setContentListCssSelector(contentListCssSelector);
        fetchConfigInfo.setFieldsCssSelector(fieldsCssSelectorMap);

        Spider spider = Spider.create(new TianMaoSearchPageProcessor(fetchConfigInfo));
        SeleniumDownloader seleniumDownloader =
                new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        spider.setDownloader(seleniumDownloader);

//        spider.addUrl("https://search.jd.com/Search?keyword=%E5%9B%BE%E4%B9%A6&enc=utf-8&suggest=1.def.0.V17&wq=tushu&pvid=a2b6ae1c101440f4bb815bc650e951f3");
        spider.addUrl("https://search.jd.com/Search?keyword=%E7%94%B5%E5%99%A8&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.def.0.V17&wq=dianqi&stock=1&page=197&s=5878&click=0");



        spider.addPipeline(new DataPipeline()).run();
    }
}

package com.pinganfu.crawler.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.download.SeleniumDownloader;
import com.pinganfu.crawler.fetcher.processor.TianMaoSearchPageProcessor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        spider.addUrl("https://search.jd.com/Search?keyword=%E5%9B%BE%E4%B9%A6&enc=utf-8&suggest=1.def.0.V17&wq=tushu&pvid=a2b6ae1c101440f4bb815bc650e951f3");



        spider.addPipeline(new DataPipeline()).run();
    }
}

package com.pinganfu.crawler.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.processor.JDSearchPageProcessor;
import com.pinganfu.crawler.fetcher.processor.TianMaoSearchPageProcessor;
import com.pinganfu.crawler.fetcher.proxy.DefaultProxyProvider;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.PhantomJSDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        System.setProperty("selenuim_config", "D:\\wuhao\\webmagic\\config.ini");
        SeleniumDownloader seleniumDownloader =
                new SeleniumDownloader("D:\\wuhao\\webmagic\\chromedriver.exe");
//        spider.setDownloader(httpClientDownloader);

        PhantomJSDownloader phantomJSDownloader = new PhantomJSDownloader("phantomjs.exe","D:\\wuhao\\webmagic\\crawl.js");
        spider.setDownloader(phantomJSDownloader);
        //目标url模版
        String urlTemplate = (String)jobDataMap.get("urlTemplate");
        //目标url配置
        String pageParams = (String)jobDataMap.get("pageParams");
        //总页数
        int pageSize = (int)jobDataMap.get("pageSize");
        targetUrlAdd(spider, urlTemplate, pageParams, pageSize);

        spider.addPipeline(new DataPipeline()).run();
    }


    private void targetUrlAdd(Spider spider, String urlTemplate, String pageParams, int pageSize){
        if(urlTemplate==null || "".equals(urlTemplate)){
            return;
        }
        if(pageParams==null || "".equals(pageParams)){
            return;
        }
        try {
            urlTemplate = URLDecoder.decode(urlTemplate,"gbk");

            Map<String,Integer> pageParamsMap = (Map<String,Integer>) JSON.parse(pageParams);
            Map<String,Integer> paramsMap = new HashMap<String,Integer>();

            for(int pageNum=1;pageNum <=pageSize; pageNum++){
                Iterator<String> iterator = pageParamsMap.keySet().iterator();
                StringBuffer stringBuffer = new StringBuffer();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    paramsMap.put(key,pageParamsMap.get(key)*pageNum);
                }
                Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
                Matcher matcher = pattern.matcher(urlTemplate);
                while(matcher.find()){
                    String key = matcher.group(1);
                    String value = null;
                    if(paramsMap.get(key)!=null){
                        value = paramsMap.get(key).toString();
                    }
                    if(value == null){
                        value = "";
                    }else{
                        value = value.replaceAll("\\$", "\\\\\\$");
                    }
                    matcher.appendReplacement(stringBuffer, value);
                }
                matcher.appendTail(stringBuffer);

                spider.addUrl(stringBuffer.toString());
            }
        } catch (UnsupportedEncodingException e) {
        }
    }
}

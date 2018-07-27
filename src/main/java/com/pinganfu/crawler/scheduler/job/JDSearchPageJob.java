package com.pinganfu.crawler.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.processor.JDSearchPageProcessor;
import com.pinganfu.crawler.fetcher.proxy.DefaultProxyProvider;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用了Spider默认的下载器
 * 没有给downloader设置代理 等一系列反扒设置
 * TODO 代理ip,userAgent
 */
public class JDSearchPageJob implements Job {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

        Spider spider = Spider.create(new JDSearchPageProcessor(fetchConfigInfo));
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(new DefaultProxyProvider());
        spider.setDownloader(httpClientDownloader);

//        System.setProperty("selenuim_config", "D:\\wuhao\\webmagic\\config.ini");
//        SeleniumDownloader seleniumDownloader =
//                new SeleniumDownloader("D:\\wuhao\\webmagic\\chromedriver.exe");
//        spider.setDownloader(seleniumDownloader);


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
            urlTemplate = URLDecoder.decode(urlTemplate,"utf-8");

            Map<String,Integer> pageParamsMap = (Map<String,Integer>) JSON.parse(pageParams);
            Map<String,Integer> paramsMap = new HashMap<String,Integer>();

            for(int pageNum=1;pageNum <=pageSize; pageNum++){

                Iterator<String> iterator = pageParamsMap.keySet().iterator();
                StringBuffer stringBuffer = new StringBuffer();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    paramsMap.put(key,pageParamsMap.get(key)*(pageNum-1)+1);
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
            LOGGER.warn("错误的字符串编码");
        }
    }
}

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


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
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
        String taskName = (String)jobDataMap.get("taskName");
        String contentListCssSelector = (String)jobDataMap.get("contentListCssSelector");
        Map fieldsCssSelectorMap = (Map)jobDataMap.get("fieldsCssSelector");

        FetchConfigInfo fetchConfigInfo = new FetchConfigInfo();
        fetchConfigInfo.setBatchNo(taskId+System.currentTimeMillis());
        fetchConfigInfo.setTaskName(taskName);
        fetchConfigInfo.setContentListCssSelector(contentListCssSelector);
        fetchConfigInfo.setFieldsCssSelector(fieldsCssSelectorMap);

        Spider spider = Spider.create(new JDSearchPageProcessor(fetchConfigInfo));

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(new DefaultProxyProvider());
        spider.setDownloader(httpClientDownloader);

        //目标url模版
        String urlTemplate = (String)jobDataMap.get("urlTemplate");
        //目标url配置
        String pageParams = (String)jobDataMap.get("pageParams");
        //总页数
        int pageSize = (int)jobDataMap.get("pageSize");

        Map<String,Integer> pageParamsMap = (Map<String,Integer>) JSON.parse(pageParams);

        targetUrlAdd(spider, urlTemplate, pageParamsMap, pageSize);

        spider.addPipeline(new DataPipeline()).run();
    }

    /**
     * 获取京东搜索页面字符串的编码
     * 默认为gbk
     * @param urlTemplate
     * @return
     */
    private String getEncodeOfTempUrl(String urlTemplate){
        Pattern pattern = Pattern.compile("enc=(.*?)&", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(urlTemplate);
        if (matcher.find()) {
            String encode = matcher.group(1);
            if (Charset.isSupported(encode)) {
               return encode;
            }
        }
        return "utf-8";

    }

    /**
     * 添加目标url
     * @param spider
     * @param urlTemplate 分页url模版
     * @param pageParamsMap 分页url需要替换的参数
     * @param pageSize 分页总页数
     */
    private void targetUrlAdd(Spider spider, String urlTemplate, Map<String,Integer> pageParamsMap, int pageSize){
        if(urlTemplate==null || "".equals(urlTemplate)){
            return;
        }
        if(pageParamsMap==null || pageParamsMap.isEmpty()){
            return;
        }
        try {
            urlTemplate = URLDecoder.decode(urlTemplate, getEncodeOfTempUrl(urlTemplate));

            Map<String,Integer> targetParamsMap = new HashMap<String,Integer>();
            for(int pageNum=1; pageNum <= pageSize; pageNum++){
                Iterator<String> iterator = pageParamsMap.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    targetParamsMap.put(key, pageParamsMap.get(key)*(pageNum-1)+1);
                }
                spider.addUrl(pageParamsReplace(urlTemplate, targetParamsMap));
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("错误的字符串编码");
        }
    }

    /**
     * 分页链接参数替换
     * 将模板中需要替换的参数替换成目标参数值
     * @param urlTemplate 分页链接模版
     * @param targetParamsMap 替换的参数内容
     * @return
     */
    private String pageParamsReplace(String urlTemplate, Map<String,Integer> targetParamsMap){
        StringBuffer stringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(urlTemplate);
        while(matcher.find()){
            String key = matcher.group(1);
            String value = null;
            if(targetParamsMap.get(key)!=null){
                value = targetParamsMap.get(key).toString();
            }
            if(value == null){
                value = "";
            }else{
                value = value.replaceAll("\\$", "\\\\\\$");
            }
            matcher.appendReplacement(stringBuffer, value);
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}

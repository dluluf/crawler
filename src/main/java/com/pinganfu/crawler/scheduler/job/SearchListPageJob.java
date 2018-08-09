package com.pinganfu.crawler.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.pinganfu.crawler.data.dao.TaskDao;
import com.pinganfu.crawler.data.dao.UrlConfigDao;
import com.pinganfu.crawler.data.model.TaskDO;
import com.pinganfu.crawler.data.model.UrlConfigDO;
import com.pinganfu.crawler.data.pipeline.DataPipeline;
import com.pinganfu.crawler.fetcher.download.PageParamsDownloader;
import com.pinganfu.crawler.fetcher.download.SeleniumDownloader;
import com.pinganfu.crawler.fetcher.processor.PageParamsProcessor;
import com.pinganfu.crawler.fetcher.processor.SearchListPageProcessor;
import com.pinganfu.crawler.util.SpringContextUtil;
import org.jsoup.nodes.Document;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchListPageJob implements Job {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private TaskDao taskDao;
    private UrlConfigDao urlConfigDao;


    public SearchListPageJob(){
        this.taskDao =  (TaskDao)SpringContextUtil.getBean("taskDao");
        this.urlConfigDao =  (UrlConfigDao) SpringContextUtil.getBean("urlConfigDao");
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
        return "gbk";

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

    /**
     * 添加目标url
     * @param urlTemplate 分页url模版
     * @param pageParamsMap 分页url需要替换的参数
     * @param pageSize 分页总页数
     */
    private List<String> getTargetUrls(String urlTemplate, Map<String,Integer> pageParamsMap, int pageSize){
        List<String> urlList = new ArrayList<String>();
        if(pageParamsMap==null){
            LOGGER.error("分页参数为空，请检查配置");
            return urlList;
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
                urlList.add(pageParamsReplace(urlTemplate, targetParamsMap));
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("错误的字符串编码");
        }
        return urlList;
    }

    /**
     * 要求如果 总页数 的css选择器如果选择的是元素的属性值，
     * 则要求css选择器的内容最后为属性名
     * @param urlConfigDO
     * @param taskDO
     * @param downloader
     * @return
     */
    private int getRequestUrlPageSize(UrlConfigDO urlConfigDO,TaskDO taskDO,PageParamsDownloader downloader){
        String url = taskDO.getSeedUrl();
        Page page = downloader.download(new Request(url),Spider.create(new PageParamsProcessor()));
        Document document = page.getHtml().getDocument();
        //TODO 这里pageSize的处理只做了天猫和京东的处理
        // 或者说针对每种商户进行不同的处理是容许的（商户数量有限），这里商户的不同页面展示页面的参数基本一致
        // 否则，可以看出把pageSize放在task表中设置 比较合适
        String pageSize  = "";
        String cssSelect = urlConfigDO.getPageCountCssSelector();

        if(cssSelect.lastIndexOf("]")!=-1){
            String attr = cssSelect.substring(cssSelect.lastIndexOf("[")+1,cssSelect.lastIndexOf("]"));
            pageSize = document.select(urlConfigDO.getPageCountCssSelector()).attr(attr);
        }else{
            pageSize = document.select(urlConfigDO.getPageCountCssSelector()).text();
        }
        LOGGER.info("抓取页面"+url+"的总页数为："+pageSize);
        return Integer.valueOf(pageSize);
    }
    /**
     * 1.通过种子url，爬取这一页中的一些特定信息：如 总页数
     * 2.通过配置信息，以及总页数 构造目标url
     * 3.创建新的爬虫去爬取这些url
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("抓取任务开始。。。");
        //获取所有需要执行的(任务跑批时间 状态符合要求的)任务
        List<TaskDO> taskDOS = taskDao.selectAll();
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for(TaskDO taskDO:taskDOS){

            SearchListPageProcessor processor = new SearchListPageProcessor();
            Spider spider = Spider.create(processor);
            //通过taskDO 的 seedUrl 获取对应的url配置表信息
            String domainUrl = UrlUtils.getDomain(taskDO.getSeedUrl());
            UrlConfigDO urlConfigDO = urlConfigDao.selectByPrimaryKey(null,domainUrl);
            //TODO DOWNLOADER这里需要考虑一下
            PageParamsDownloader downloader = new PageParamsDownloader();
            downloader.setUrlConfigDO(urlConfigDO);
            String js = urlConfigDO.getJavascript();

            int pageSize = getRequestUrlPageSize(urlConfigDO,taskDO,downloader);

            //模版不为空，获取拼接参数，将url添加到spider中
            String urlTemplate = taskDO.getUrlTemplate();
            if(urlTemplate!= null && !"".equals(urlTemplate)){
                Map<String,Integer> pageParamsMap = (Map<String,Integer>) JSON.parse(taskDO.getPageParams());

                List<String> urlList = getTargetUrls(urlTemplate, pageParamsMap, pageSize);
                LOGGER.info("任务："+taskDO.getTaskName()+"的种子url有"+urlList.size()+"条");
                for (int i = 0; i<urlList.size(); i++){
                    spider.addUrl(urlList.get(i));
                }
                //使用默认下载器HttpClientDownloader
                //TODO 可以配置请求头 代理ip
                //需要模拟鼠标操作 使用模拟浏览器的downloader
                if(js != null && !"".equals(js)){
                    spider.setDownloader(downloader);
                }

            }else{
                //走最初的流程 通过种子url，获取下一页链接，继续访问下一页内容
                if(js != null && !"".equals(js)){
                    LOGGER.info("任务："+taskDO.getTaskName()+"没有模版，需要模拟鼠标行为，使用SeleniumDownloader下载器");
                    SeleniumDownloader seleniumDownloader = new SeleniumDownloader();
                    seleniumDownloader.setJs(js);
                    spider.setDownloader(seleniumDownloader);
                }
                spider.addUrl(taskDO.getSeedUrl());
            }

            processor.setUrlConfigDO(urlConfigDO);
            spider.addPipeline(new DataPipeline());
            pool.submit(spider);
        }
    }

    public static void main(String[] args) {
        PageParamsDownloader pageParamsDownloader = new PageParamsDownloader();
        UrlConfigDO urlConfigDO = new UrlConfigDO();
        urlConfigDO.setPageCountCssSelector(".p-skip b");
        urlConfigDO.setJavascript("window.scrollBy(0,6000)");
        urlConfigDO.setSleepTime(1000);
        pageParamsDownloader.setUrlConfigDO(urlConfigDO);
        String url  = "https://search.jd.com/Search?keyword=%E7%89%9B%E5%A5%B6&enc=utf-8&suggest=2.def.0.V17&wq=niunai&pvid=4f98b34ca9774a4da5ed93f3e9337014";
        Page page = pageParamsDownloader.download(new Request(url),Spider.create(new PageParamsProcessor()));
        Document document = page.getHtml().getDocument();
        String pagesize = document.select(".p-skip b").text();
        System.out.println(pagesize);
    }

}

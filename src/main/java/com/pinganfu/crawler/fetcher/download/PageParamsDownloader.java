package com.pinganfu.crawler.fetcher.download;

import com.pinganfu.crawler.data.model.UrlConfigDO;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public class PageParamsDownloader implements Downloader,Closeable {
    private Logger logger = Logger.getLogger(this.getClass());

    private volatile WebDriverPool webDriverPool;

    private int defaultPoolSize = 1;

    private UrlConfigDO urlConfigDO;


    public PageParamsDownloader() {
        System.getProperties().setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(defaultPoolSize);
            }
        }
    }

    public Page download(Request request, Task task) {
        WebDriver webDriver = null;
        Page page = new Page();
        try {
            webDriver = webDriverPool.get();
            WebDriver.Options manage = webDriver.manage();
            manage.window().maximize();

            Site site = task.getSite();
            if (site.getCookies() != null) {
                for (Map.Entry<String, String> cookieEntry : site.getCookies()
                        .entrySet()) {
                    Cookie cookie = new Cookie(cookieEntry.getKey(),
                            cookieEntry.getValue());
                    manage.addCookie(cookie);
                }
            }
            webDriver.get(request.getUrl());

            JavascriptExecutor jsExecutor=(JavascriptExecutor) webDriver;
            if(urlConfigDO.getJavascript()!=null && !"".equals(urlConfigDO.getJavascript())){
                jsExecutor.executeScript(urlConfigDO.getJavascript());
                Thread.sleep(urlConfigDO.getSleepTime());
            }


            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            page.setRawText(content);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
        }catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("interrupted", e);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("download error...", e);
        }
        webDriverPool.returnToPool(webDriver);
        return page;

    }

    @Override
    public void setThread(int threadNum) {
        this.defaultPoolSize = threadNum;
    }


    @Override
    public void close() throws IOException {
        webDriverPool.closeAll();
    }

    public UrlConfigDO getUrlConfigDO() {
        return urlConfigDO;
    }

    public void setUrlConfigDO(UrlConfigDO urlConfigDO) {
        this.urlConfigDO = urlConfigDO;
    }
}

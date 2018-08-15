package com.pinganfu.crawler.fetcher.download;

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

public class SeleniumDownloader implements Downloader,Closeable{
    private Logger logger = Logger.getLogger(this.getClass());
    private volatile WebDriverPool webDriverPool;
    private int defaultPoolSize = 1;
    public String js;

    public SeleniumDownloader() {
        System.getProperties().setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(defaultPoolSize);
            }
        }
    }
    public SeleniumDownloader(String chromeDriverPath) {
        System.getProperties().setProperty("webdriver.chrome.driver", chromeDriverPath);
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
            if(js!=null){
                jsExecutor.executeScript(js);
            }

            Thread.sleep(3000);
            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            page.setRawText(content);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            //TODO 这里用发现对应元素的方式来判断是否抓取到末尾；
            // 是不是通过页面的总数 来设定比较好呢。因为天猫的末尾元素是一样的，这样通过总页数来判断，比较合适，
            // 而且可能时间上会节约几秒
            try{
                WebElement lastNext = webDriver.findElement(By.cssSelector(".pn-next.disabled"));
                if(lastNext != null){
                    logger.info("this page is the last of the search list...");
                    webDriverPool.returnToPool(webDriver);
                    return page;
                }
            }catch (org.openqa.selenium.NoSuchElementException e){
                logger.info("NO ELEMENT WITH CSS SELECTOR:.pn-next.disabled");
            }

            WebElement element = webDriver.findElement(By.cssSelector(".pn-next"));
            if(element !=null){
                element.click();
                String url  = webDriver.getCurrentUrl();
                page.addTargetRequest(new Request(url));
            }

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

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }
}

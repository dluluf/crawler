package com.pinganfu.crawler.fetcher.download;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.util.Map;

public class SeleniumDownloader implements Downloader{


    private Logger logger = Logger.getLogger(getClass());

    private static WebDriver webDriver;

    public SeleniumDownloader(String chromeDriverPath) {
        System.getProperties().setProperty("webdriver.chrome.driver",
                chromeDriverPath);
        webDriver = new ChromeDriver();
    }


    public Page download(Request request, Task task) {

        Page page = new Page();
        try{

            webDriver.manage().window().maximize();
            webDriver.get(request.getUrl());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebDriver.Options manage = webDriver.manage();
            Site site = task.getSite();
            if (site.getCookies() != null) {
                for (Map.Entry<String, String> cookieEntry : site.getCookies()
                        .entrySet()) {
                    Cookie cookie = new Cookie(cookieEntry.getKey(),
                            cookieEntry.getValue());
                    manage.addCookie(cookie);
                }
            }
            JavascriptExecutor jsExecutor=(JavascriptExecutor) webDriver;
            jsExecutor.executeScript("window.scrollBy(0,7000)");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            page.setRawText(content);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            try{
                WebElement lastNext = webDriver.findElement(By.cssSelector(".pn-next.disable"));
                if(lastNext != null){
                    webDriver.quit();
                    return page;
                }
            }catch (org.openqa.selenium.NoSuchElementException e){

            }
            WebElement element = webDriver.findElement(By.cssSelector(".pn-next"));
            if(element !=null){
                element.click();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String url  = webDriver.getCurrentUrl();
                System.out.println(url);
                page.addTargetRequest(new Request(url));
            }
        }catch (Exception e){
            webDriver.quit();
        }
        return page;

    }

    @Override
    public void setThread(int threadNum) {

    }




}

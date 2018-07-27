package com.pinganfu.crawler.fetcher.processor;

import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.model.GoodsDO;
import com.pinganfu.crawler.fetcher.userAgent.DefaultUserAgentProvider;
import com.pinganfu.crawler.util.SnowflakeIdWorker;
import com.pinganfu.crawler.util.SpringContextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TianMaoSearchPageProcessor implements PageProcessor {
    private Site site;
    private Set<Cookie> cookies;

    private static FetchConfigInfo fetchConfigInfo;

    private SnowflakeIdWorker snowflakeIdWorker;

    public TianMaoSearchPageProcessor(FetchConfigInfo fetchConfigInfo) {
        this.fetchConfigInfo = fetchConfigInfo;
        snowflakeIdWorker = (SnowflakeIdWorker) SpringContextUtil.getBean("snowflakeIdWorker");
    }

    private void parseFields(Document document, List<GoodsDO> goodsDOList) {
        String contentListCssSelector = fetchConfigInfo.getContentListCssSelector();
        Map<String, String> fieldsCssSelectorMap = fetchConfigInfo.getFieldsCssSelector();

        if (contentListCssSelector != null && !"".equals(contentListCssSelector)) {
            Elements contentList = document.select(contentListCssSelector);
            for (Element content : contentList) {
                GoodsDO goodsDO = new GoodsDO();
                goodsDO.setId(String.valueOf(snowflakeIdWorker.nextId()));
                goodsDO.setGoodsBatchNo(fetchConfigInfo.getBatchNo());
                String nameField = fieldsCssSelectorMap.get("name");
                if (!StringUtils.isEmpty(nameField)) {
                    goodsDO.setGoodsName(content.select(nameField).text());
                }
                String goodPriceField = fieldsCssSelectorMap.get("goodPrice");
                if (!StringUtils.isEmpty(goodPriceField)) {
                    goodsDO.setGoodsPrice(content.select(goodPriceField).text());
                }
                String goodTypeField = fieldsCssSelectorMap.get("goodType");
                if (!StringUtils.isEmpty(goodTypeField)) {
                    goodsDO.setGoodsType(content.select(goodTypeField).text());
                }
                String commentField = fieldsCssSelectorMap.get("comment");
                if (!StringUtils.isEmpty(commentField)) {
                    goodsDO.setGoodsCommentCount(content.select(commentField).text());
                }
                String skuField = fieldsCssSelectorMap.get("sku");
                if (!StringUtils.isEmpty(skuField)) {
                    goodsDO.setGoodsSku(content.select(skuField).text());
                }
                String salesField = fieldsCssSelectorMap.get("sales");
                if (!StringUtils.isEmpty(salesField)) {
                    goodsDO.setGoodsSales(content.select(salesField).text());
                }
                goodsDOList.add(goodsDO);
            }
        }
    }

    @Override
    public void process(Page page) {

        Document document = page.getHtml().getDocument();
        List<GoodsDO> goodsDOList = new ArrayList<GoodsDO>();
        parseFields(document, goodsDOList);
        page.putField("data", goodsDOList);
    }


    @Override
    public Site getSite() {
        site = Site.me().setSleepTime(1000).setTimeOut(10 * 1000);
        String domain = "list.tmall.com";
        site.setDomain(domain);
        site.setUserAgent(DefaultUserAgentProvider.getUserAgent());

        System.setProperty("selenuim_config", "D:\\wuhao\\webmagic\\config.ini");
        System.setProperty("phantomjs.binary.path", "D:\\wuhao\\webmagic\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        DesiredCapabilities caps = new DesiredCapabilities();
        ((DesiredCapabilities)caps).setJavascriptEnabled(true);
        ((DesiredCapabilities)caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"D:\\wuhao\\webmagic\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        PhantomJSDriver webDriver = new PhantomJSDriver(caps);
        webDriver.get("https://login.taobao.com/");
        webDriver.executePhantomJS("D:\\wuhao\\webmagic\\crawl.js");
        login(webDriver);
        cookies = webDriver.manage().getCookies();
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie coo = iterator.next();
            site.addCookie(domain, coo.getName(), coo.getValue());
        }

        return site;
    }



    public static void login(PhantomJSDriver webDriver) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webDriver.findElement(By.id("J_Quick2Static")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webDriver.findElement(By.id("TPL_username_1")).sendKeys("tb5275960");
        webDriver.findElement(By.id("TPL_password_1")).sendKeys("xiuning7833177");


        webDriver.findElement(By.id("J_SubmitStatic")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
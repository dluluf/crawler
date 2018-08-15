package com.pinganfu.crawler.fetcher.processor;

import com.pinganfu.crawler.data.model.ProxyIPDO;
import com.pinganfu.crawler.fetcher.userAgent.DefaultUserAgentProvider;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class XICIPageProcessor implements PageProcessor{
    private Site site;

    @Override
    public void process(Page page) {
        Document document = page.getHtml().getDocument();
        Elements elements =document.select("#ip_list tbody tr");
        List<ProxyIPDO> list = new ArrayList<>();
        for(int i= 1;i<elements.size();i++){
            ProxyIPDO proxyIPDO = new ProxyIPDO();
            Element content =  elements.get(i);
            String speed =  content.select("tr td:nth-child(7) div[title]").attr("title");
            String speedSecond = speed.substring(0,speed.indexOf("秒"));
            if(Double.valueOf(speedSecond)<1){
                String host = content.select("tr td:nth-child(2)").text();
                String portStr = content.select("tr td:nth-child(3)").text();

                proxyIPDO.setHost(host);
                proxyIPDO.setPort(Integer.valueOf(portStr));
                list.add(proxyIPDO);
            }
        }
        page.putField("data", list);
    }

    @Override
    public Site getSite() {
        site = Site.me().setSleepTime(2).setTimeOut(3000);
        site.setUserAgent(DefaultUserAgentProvider.getUserAgent());
        return site;
    }
}

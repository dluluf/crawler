package com.pinganfu.crawler.fetcher.processor;

import com.pinganfu.crawler.fetcher.userAgent.DefaultUserAgentProvider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 通过css选择器
 * 抓取任务的总页数
 */
public class PageParamsProcessor implements PageProcessor {
    private Site site;

    @Override
    public void process(Page page) {}

    @Override
    public Site getSite() {
        site = Site.me().setTimeOut(3000);
        site.setUserAgent(DefaultUserAgentProvider.getUserAgent());
        return site;
    }

}

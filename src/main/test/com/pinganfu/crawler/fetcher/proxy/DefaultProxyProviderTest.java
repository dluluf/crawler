package com.pinganfu.crawler.fetcher.proxy;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;

import static org.junit.Assert.*;

public class DefaultProxyProviderTest {
    private DefaultProxyProvider defaultProxyProvider =new DefaultProxyProvider();
    @Test
    public void returnProxy() {
        Proxy proxy = new Proxy("123.34.44.55",80,"aaa","888");
        Page page = new Page();
        defaultProxyProvider.returnProxy(proxy, page, new Task() {
            @Override
            public String getUUID() {
                return null;
            }

            @Override
            public Site getSite() {
                return null;
            }
        });
    }

    @Test
    public void getProxy() {


        defaultProxyProvider.getProxy(new Task() {
            @Override
            public String getUUID() {
                return null;
            }

            @Override
            public Site getSite() {
                return null;
            }
        });
    }
}
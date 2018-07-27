package com.pinganfu.crawler.fetcher.proxy;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DefaultProxyProvider implements ProxyProvider{

    private final List<Proxy> proxies;
    public DefaultProxyProvider(){
        //TODO 加载代理
        proxies = new ArrayList<>();
    }

    /**
     * 下载网页处理完后，对代理ip进行处理
     * 如：对不可用的代理ip（被拒绝的ip）从代理池中删除
     * @param proxy
     * @param page
     * @param task
     */
    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {
       /* if(proxy==null){
            return;
        }
        Iterator<Proxy> iterator = proxies.iterator();
        while(iterator.hasNext()){
            Proxy proxyInfo = iterator.next();
            if(proxy.getHost().equals(proxyInfo.getHost())){
                iterator.remove();
            }
        }*/
    }

    /**
     * 随机从代理池中获取一个代理的方式
     * @param task
     * @return
     */
    @Override
    public Proxy getProxy(Task task) {
        if(proxies!=null && proxies.size()!=0){
            Random random = new Random();
            return proxies.get(random.nextInt(proxies.size()));
        }
        return null;

    }
}

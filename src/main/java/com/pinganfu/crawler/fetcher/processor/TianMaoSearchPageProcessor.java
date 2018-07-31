package com.pinganfu.crawler.fetcher.processor;

import com.pinganfu.crawler.data.model.FetchConfigInfo;
import com.pinganfu.crawler.data.model.GoodsDO;
import com.pinganfu.crawler.fetcher.userAgent.DefaultUserAgentProvider;
import com.pinganfu.crawler.util.SnowflakeIdWorker;
import com.pinganfu.crawler.util.SpringContextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TianMaoSearchPageProcessor implements PageProcessor {
    private Site site;

    private static FetchConfigInfo fetchConfigInfo;

    private SnowflakeIdWorker snowflakeIdWorker;

    public TianMaoSearchPageProcessor(FetchConfigInfo fetchConfigInfo){
        this.fetchConfigInfo = fetchConfigInfo;
        snowflakeIdWorker = (SnowflakeIdWorker) SpringContextUtil.getBean("snowflakeIdWorker");
    }

    private void parseFields(Page page, Document document,List<GoodsDO> goodsDOList){
        String contentListCssSelector = fetchConfigInfo.getContentListCssSelector();
        Map<String,String> fieldsCssSelectorMap = fetchConfigInfo.getFieldsCssSelector();

        if(contentListCssSelector != null && !"".equals(contentListCssSelector)){
            Elements contentList =  document.select(contentListCssSelector);
            for(Element content : contentList){
                GoodsDO goodsDO = new GoodsDO();
                goodsDO.setId(String.valueOf(snowflakeIdWorker.nextId()));
                goodsDO.setGoodsBatchNo(fetchConfigInfo.getBatchNo());

                String goodsDetailUrl = fieldsCssSelectorMap.get("goodsDetailUrl");
                if(!StringUtils.isEmpty(goodsDetailUrl)){
                    String detailUrl = content.select(goodsDetailUrl).attr("abs:href");
                    goodsDO.setGoodsDetailUrl(detailUrl);
                    //这里是否需要爬取根据需求来定
                    //page.addTargetRequest(detailUrl);
                }
                String nameField = fieldsCssSelectorMap.get("goodsName");
                if(!StringUtils.isEmpty(nameField)){
                    goodsDO.setGoodsName(content.select(nameField).text());
                }
                String goodPriceField = fieldsCssSelectorMap.get("goodsPrice");
                if(!StringUtils.isEmpty(goodPriceField)){
                    goodsDO.setGoodsPrice(content.select(goodPriceField).text());
                }
                goodsDO.setGoodsType(fetchConfigInfo.getTaskName());

                String commentField = fieldsCssSelectorMap.get("commentNum");
                if(!StringUtils.isEmpty(commentField)){
                    goodsDO.setGoodsCommentCount(content.select(commentField).text());
                }
                String shopName = fieldsCssSelectorMap.get("shopName");
                if(!StringUtils.isEmpty(shopName)){
                    goodsDO.setShopName(content.select(shopName).text());
                }
                String skuField = fieldsCssSelectorMap.get("sku");
                if(!StringUtils.isEmpty(skuField)){
                    goodsDO.setGoodsSku(content.select(skuField).text());
                }
                String salesField = fieldsCssSelectorMap.get("sales");
                if(!StringUtils.isEmpty(salesField)){
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
        //根据请求的域名来解析不同的url页面
        String domain = UrlUtils.getDomain(page.getRequest().getUrl());
        if("search.jd.com".equals(domain)){
            parseFields(page, document, goodsDOList);
        }else if("item.jd.com".equals(domain)){
            //TODO 解析第二级页面内容
        }
        page.putField("data", goodsDOList);
    }


    @Override
    public Site getSite() {
        site = Site.me().setSleepTime(2000).setTimeOut(3000);
        site.setUserAgent(DefaultUserAgentProvider.getUserAgent());
        return site;
    }

}
package com.pinganfu.crawler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CssSelectTest {
    @Test
    public void test() throws IOException {
        File file = new File("D:\\Users\\ex-wangnaipeng001\\Desktop\\新建文件夹 (2)\\ccc.txt");
        Document document = Jsoup.parse(file,"utf-8");
        Elements elements = document.select(".pn-next.disabled");
        Element element = elements.get(0);
        System.out.println(element.text());
    }
}

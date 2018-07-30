package com.pinganfu.crawler.scheduler.job;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class JDSearchPageJobTest {

    @Test
    public void execute() {
        String urlTemplate ="https://search.jd.com/Search?keyword=%E5%9B%BE%E4%B9%A6&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E5%9B%BE%E4%B9%A6&page=${page}&s=${s}&click=0";
        Pattern pattern = Pattern.compile("enc=(.*?)&", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(urlTemplate);
        if (matcher.find()) {
            String encode = matcher.group(1);
            if (Charset.isSupported(encode)) {
                System.out.println(encode);
            }
        }

    }
}
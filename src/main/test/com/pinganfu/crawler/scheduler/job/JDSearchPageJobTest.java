package com.pinganfu.crawler.scheduler.job;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
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

    @Test
    public void pageParamsReplace() {
        String urlTemplate = "https://search.jd.com/Search?keyword=%E5%9B%BE%E4%B9%A6&enc=utf-8&" +
                "qrst=1&rt=1&stop=1&vt=2&wq=%E5%9B%BE%E4%B9%A6&page=${page}&s=${s}&click=0";

        Map<String,Integer> targetParamsMap =new HashMap<>();
        targetParamsMap.put("page",11);
        targetParamsMap.put("s",2);

        StringBuffer stringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(urlTemplate);
        while(matcher.find()){
            String key = matcher.group(1);
            String value = null;
            if(targetParamsMap.get(key)!=null){
                value = targetParamsMap.get(key).toString();
            }
            if(value == null){
                value = "";
            }else{
                value = value.replaceAll("\\$", "\\\\\\$");
            }
            matcher.appendReplacement(stringBuffer, value);
        }
        matcher.appendTail(stringBuffer);
        System.out.println(stringBuffer.toString());
    }
}
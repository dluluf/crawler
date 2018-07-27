package com.pinganfu.crawler.fetcher.userAgent;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultUserAgentProviderTest {

    @Test
    public void getUserAgent() {
//        DefaultUserAgentProvider defaultUserAgentProvider = new DefaultUserAgentProvider();
        for(int i=0;i<10;i++){
            String result = DefaultUserAgentProvider.getUserAgent();
            System.out.println(result);
        }

    }
}
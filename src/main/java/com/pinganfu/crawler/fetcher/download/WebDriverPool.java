package com.pinganfu.crawler.fetcher.download;

import com.pinganfu.crawler.data.model.ProxyIPDO;
import com.pinganfu.crawler.fetcher.proxy.DefaultProxyProvider;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.aop.framework.ProxyFactory.getProxy;

class WebDriverPool {
	private Logger logger = Logger.getLogger(getClass());

	private final static int DEFAULT_CAPACITY = 5;

	private final int capacity;

	private final static int STAT_RUNNING = 1;

	private final static int STAT_CLOSED = 2;

	private AtomicInteger stat = new AtomicInteger(STAT_RUNNING);

	/**
	 * 线程数增加后，如果超出list中的webDriver量，则新增chromeDriver数
	 */
	private List<WebDriver> webDriverList = Collections.synchronizedList(new ArrayList<WebDriver>());
	/**
	 * 存储webDriver的队列
	 */
	private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>();

	public WebDriverPool() {
		this(DEFAULT_CAPACITY);
	}

	public WebDriverPool(int capacity) {
		this.capacity = capacity;
	}

	private static DefaultProxyProvider defaultProxyProvider = new DefaultProxyProvider();
	public WebDriver get() throws InterruptedException {
		checkRunning();
		WebDriver poll = innerQueue.poll();
		ChromeDriver chromeDriver = null;
		if (poll != null) {
			chromeDriver= (ChromeDriver)poll;

			us.codecraft.webmagic.proxy.Proxy proxy = defaultProxyProvider.getProxy(null);
			if(proxy!=null){
//				String proxyIpAndPort = proxy.getHost()+":"+proxy.getPort();
				String proxyIpAndPort = "33.33.33.33:33";
				Proxy proxy2=new Proxy();
//				proxy2.setHttpProxy(proxyIpAndPort);
				proxy2.setSslProxy(proxyIpAndPort);
//				proxy2.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(CapabilityType.PROXY,proxy2);
				chromeDriver.getCapabilities().merge(cap);
				logger.info("request proxy ip is "+proxyIpAndPort);
			}

			return chromeDriver;
		}
		if (webDriverList.size() < capacity) {
			synchronized (webDriverList) {
				if (webDriverList.size() < capacity) {
					ChromeOptions chromeOptions = new ChromeOptions();
//					chromeOptions.addArguments("--headless");
					chromeOptions.setHeadless(true);

					us.codecraft.webmagic.proxy.Proxy proxy = defaultProxyProvider.getProxy(null);
					if(proxy!=null){
//						String proxyIpAndPort = proxy.getHost()+":"+proxy.getHost();
						String proxyIpAndPort = "33.33.33.33:33";;
						Proxy proxy2=new Proxy();
						proxy2.setSslProxy(proxyIpAndPort);
//						proxy2.setHttpProxy(proxyIpAndPort).
//								setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
						chromeOptions.setProxy(proxy2);
					}



					ChromeDriver webDriver = new ChromeDriver(chromeOptions);


					innerQueue.add(webDriver);
					webDriverList.add(webDriver);
				}
			}
		}

		return innerQueue.take();
	}

	public void returnToPool(WebDriver webDriver) {
		checkRunning();
		innerQueue.add(webDriver);
	}

	protected void checkRunning() {
		if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
			throw new IllegalStateException("Already closed!");
		}
	}

	public void closeAll() {
		boolean b = stat.compareAndSet(STAT_RUNNING, STAT_CLOSED);
		if (!b) {
			throw new IllegalStateException("Already closed!");
		}
		for (WebDriver webDriver : webDriverList) {
			logger.info("Quit webDriver" + webDriver);
			webDriver.quit();
		}
	}

}

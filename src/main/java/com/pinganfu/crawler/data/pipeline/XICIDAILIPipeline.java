package com.pinganfu.crawler.data.pipeline;

import com.pinganfu.crawler.data.dao.ProxyIPDao;
import com.pinganfu.crawler.data.model.ProxyIPDO;
import com.pinganfu.crawler.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

public class XICIDAILIPipeline implements Pipeline {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ProxyIPDao proxyIPDao;

    public XICIDAILIPipeline() {
        proxyIPDao = (ProxyIPDao) SpringContextUtil.getBean("proxyIPDao");
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<ProxyIPDO> list = resultItems.get("data");
        if(list != null && !list.isEmpty()){
            proxyIPDao.insertBatch(list);
            LOGGER.info("goods数据入库"+list.size());
        }
    }
}

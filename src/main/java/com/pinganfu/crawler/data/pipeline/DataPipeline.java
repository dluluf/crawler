package com.pinganfu.crawler.data.pipeline;

import com.pinganfu.crawler.data.model.GoodsDO;
import com.pinganfu.crawler.data.service.GoodsService;
import com.pinganfu.crawler.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

public class DataPipeline implements Pipeline {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private GoodsService goodsService;

    public DataPipeline() {
        goodsService = (GoodsService) SpringContextUtil.getBean("goodsService");
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<GoodsDO> list = resultItems.get("data");
        if(list != null && !list.isEmpty()){
            goodsService.saveGoodsBatch(list);
            LOGGER.info("goods数据入库"+list.size());
        }
    }
}

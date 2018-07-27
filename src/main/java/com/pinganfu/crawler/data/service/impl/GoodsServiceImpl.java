package com.pinganfu.crawler.data.service.impl;

import com.pinganfu.crawler.data.dao.GoodsDao;
import com.pinganfu.crawler.data.model.GoodsDO;
import com.pinganfu.crawler.data.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public GoodsDO queryGoodsById(String id) {
        GoodsDO goodsDO = goodsDao.queryGoodsById(id);
        return goodsDO;
    }

    @Override
    public void saveGoodsBatch(List<GoodsDO> list) {
        goodsDao.insertBatch(list);
    }
}

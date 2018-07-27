package com.pinganfu.crawler.data.service;

import com.pinganfu.crawler.data.model.GoodsDO;

import java.util.List;

public interface GoodsService {

    GoodsDO queryGoodsById(String id);
    void saveGoodsBatch(List<GoodsDO> list);

}

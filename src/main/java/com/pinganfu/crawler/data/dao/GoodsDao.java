package com.pinganfu.crawler.data.dao;

import com.pinganfu.crawler.data.model.GoodsDO;

import java.util.List;

public interface GoodsDao {
    GoodsDO queryGoodsById(String id);
    void insertBatch(List<GoodsDO> list);
}

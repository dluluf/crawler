package com.pinganfu.crawler.data.dao;

import com.pinganfu.crawler.data.model.ProxyIPDO;
import java.util.List;

public interface ProxyIPDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProxyIPDO record);

    ProxyIPDO selectByPrimaryKey(Integer id);

    List<ProxyIPDO> selectAll();

    int updateByPrimaryKey(ProxyIPDO record);
}
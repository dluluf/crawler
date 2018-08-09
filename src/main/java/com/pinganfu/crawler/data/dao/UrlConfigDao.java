package com.pinganfu.crawler.data.dao;

import com.pinganfu.crawler.data.model.UrlConfigDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UrlConfigDao {
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("domainUrl") String domainUrl);

    int insert(UrlConfigDO record);

    UrlConfigDO selectByPrimaryKey(@Param("id") Integer id, @Param("domainUrl") String domainUrl);

    List<UrlConfigDO> selectAll();

    int updateByPrimaryKey(UrlConfigDO record);
}
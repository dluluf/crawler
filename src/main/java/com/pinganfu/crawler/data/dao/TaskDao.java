package com.pinganfu.crawler.data.dao;

import com.pinganfu.crawler.data.model.TaskDO;
import java.util.List;

public interface TaskDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskDO record);

    TaskDO selectByPrimaryKey(Integer id);

    List<TaskDO> selectAll();

    int updateByPrimaryKey(TaskDO record);
}
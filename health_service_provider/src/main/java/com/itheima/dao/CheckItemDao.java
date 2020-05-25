package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
   void add(CheckItem checkItem);
     Page<CheckItem> selectByCondition(String queryString);
   long findCountByCheckItemId(Integer id);

   void deleteById(Integer id);

    void update(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}

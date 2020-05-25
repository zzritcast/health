package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    Setmeal findBySetmealId(int id);

    List<Map<String,Object>> findSetmealCount();
}

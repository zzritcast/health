package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    void add(Setmeal setmeal);

    void setSetMealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    void deleteAssocication(Integer id);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void edit(Setmeal setmeal);

    Setmeal findBySetmealId(int id);
    //查询预约占比 记得改t_order表中的数量列名

    List<Map<String,Object>> findSetmealCount();



}

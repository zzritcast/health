package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    public void add(List<OrderSetting> list);

    public List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

    void addOrder(Map order, Integer[] ids);

    void deleteById(Integer id);

    void updateById(Integer id);

    PageResult pageQuery(QueryPageBean queryPageBean);

    PageInfo findOrderPages(Integer currentPage, Integer pageSize, String queryString);
}

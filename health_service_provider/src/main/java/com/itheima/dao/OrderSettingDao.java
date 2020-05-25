package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //增加预约
    public void add(OrderSetting orderSetting);
    //更新可预约人数
    public void editNumberByOrderDate(OrderSetting orderSetting);
    //通过日期判断 返回数量
    public long findCountByOrderDate(Date orderDate);
// <!--根据日期范围查询-->
    public List<OrderSetting> getOrderSettingByMonth(Map map);
    //通过日期返回对象
    OrderSetting findByOrderDate(Date date);
    //更新预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
//通过预约id查询单个对象
    OrderSetting findByOrderId(Integer id);

    Page<CheckItem> selectByCondition(String queryString);
}

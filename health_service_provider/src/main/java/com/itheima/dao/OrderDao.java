package com.itheima.dao;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    //增加
    public void add(Order order);
    //通过动态查询
    public List<Order> findByCondition(Order order);
    //根据预约id查询预约相关信息 （体检人姓名 预约日期  预约套餐名称  预约类型）
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();
    //通过预约id预约详情
    int deleteById(Integer id);

    void editStatus(Integer id);
    //动态查询
    List<Map<String,Object>> findByConditionMany(String queryString);

    int findByMidAndDate(Order order);
}

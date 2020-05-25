package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 预约设置
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImp implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private OrderDao orderDao;

    //文件上传，实现预约设置数据批量导入
    @Override
    public void add(List<OrderSetting> list) {
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = this.orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(countByOrderDate > 0){
                    //已经进行了预约设置，执行更新操作
                    this.orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //没有进行预约设置  执行插入操作
                    this.orderSettingDao.add(orderSetting);
                }
            }
        }
    }
    //根据月份查询对应的预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {//格式：yyyy-MM
        String begin = date + "-1";//2019-6-1
        String end = date + "-31";//2019-6-31
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //调用DAO，根据日期范围查询预约设置数据
        List<OrderSetting> list =this.orderSettingDao.getOrderSettingByMonth(map);
        //y用于存放返回前端数据的list集合
        List<Map> result = new ArrayList<>();
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> m = new HashMap<>();
                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字（几号）
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    //根据日期设置对应的预约设置数据
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = this.orderSettingDao.findCountByOrderDate(orderDate);
        if(count > 0){
            //当前日期已经进行了预约设置，需要执行更新操作
           this.orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有就那些预约设置，需要执行插入操作
            this.orderSettingDao.add(orderSetting);
        }
    }
    //分页
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //前端传入后端的数据
        //当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //每页条数
        Integer pageSize = queryPageBean.getPageSize();
        //查询条件
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkitem limit 0,10
        Page<CheckItem> page = this.orderSettingDao.selectByCondition(queryString);
        //后端返回前端的数据
        //总记录数
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        //分页结果集
        return new PageResult(total,rows);
    }

    /**
     * 添加预约信息
     *
     * @param
     * @param
     */
    @Override
    public void addOrder(Map map, Integer[] ids) {
        if (ids == null || ids.length <= 0) {
            throw new RuntimeException("预约套餐没有选择");
        }
        //查询预约用户是否已是会员
        String phoneNumber = map.get("phoneNumber").toString();
        Order order = new Order();
        //预约类型
        order.setOrderType(Order.ORDERTYPE_TELEPHONE);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //预约时间
        String orderDate = map.get("orderDate").toString();
        Date date = null;
        try {
             date = DateUtils.parseString2Date(orderDate);
            order.setOrderDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //会员信息  通过手机号
        Member member = this.memberDao.findByTelephone(phoneNumber);
        if (member == null) {
            member = new Member();
            //获取会员名字
            member.setName(map.get("name").toString());
            Date birthday = null;
            try {
                birthday = DateUtils.parseString2Date((String)map.get("birthday")) ;
                member.setBirthday(birthday);
                member.setPhoneNumber(phoneNumber);
                member.setSex(map.get("sex").toString());
                this.memberDao.add(member);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //获取会员id
        order.setMemberId(member.getId());
        //查看是否还有预约名额
        for (Integer id : ids) {
            OrderSetting orderSetting = null;
            try {
                //findOrderByDate(orderDate) 通过时间获取预约设置
                orderSetting = this.orderSettingDao.findByOrderDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (orderSetting != null) {
                order.setSetmealId(id);
                order.setOrderDate(date);
                //查看当前用户是否已经预约过这个套餐
                int total = orderDao.findByMidAndDate(order);
                if (total > 0) {
                    //重复预约了
                    throw new RuntimeException("重复预约了-->套餐id为" + id);
                }
                //查看是否当天已经预约
                this.orderDao.add(order);
                orderSetting.setNumber(orderSetting.getNumber() - 1);
                orderSetting.setReservations(orderSetting.getReservations() + 1);
                this.orderSettingDao.editNumberByOrderDate(orderSetting);
            } else {
                throw new RuntimeException("预约失败-->套餐预约数已满" + id);
            }
        }
    }
//删除预约
    @Override
    public void deleteById(Integer id) {
        OrderSetting setting = this.orderSettingDao.findByOrderId(id);
        if (setting != null) {
            int count = this.orderDao.deleteById(id);
            if (count == 0) {
                throw new RuntimeException("已确诊,无法取消");
            }
            //预约设置总数
            setting.setNumber(setting.getNumber() + 1);
            //已预约数
            setting.setReservations(setting.getReservations() - 1);
            this.orderSettingDao.editNumberByOrderDate(setting);
        }
    }
//更新
    @Override
    public void updateById(Integer id) {
        orderDao.editStatus(id);
    }

    //分页
    @Override
    public PageInfo findOrderPages(Integer currentPage, Integer pageSize, String queryString) {
        if (currentPage == null || currentPage < 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Map<String, Object>> map = this.orderDao.findByConditionMany(queryString);
        PageInfo pageInfo = new PageInfo(map);
        return pageInfo;
    }



}

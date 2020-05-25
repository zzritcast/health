package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderDao orderDao;   //预约 人 状态  类型 时间

    @Autowired
    private OrderSettingDao orderSettingDao;  //预约列表  设置

    @Autowired
    private MemberDao memberDao;  //会员


    //体检预约
    @Override
    public Result order(Map map) {

        //1.检查用户所选择的预约日期判断是否已经提前进行了预约设置，如果没有设置 不能预约
        String  orderDate = (String) map.get("orderDate");//预约时间
        try {
            OrderSetting orderSetting = this.orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));  //预约设置
            if(orderSetting == null){
                //指定日期没有进行预约设置  无法完成体检预约
                return  new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //2.检查用户所选择的预约日期是否已经预约满  如果你满了 则无法预约
            int number = orderSetting.getNumber(); //可预约人数
            int reservations = orderSetting.getReservations();  //已预约人数
            if(reservations >= number){
                //当前时间已经预约满了 无法进行预约
                return  new Result(false, MessageConstant.ORDER_FULL);
            }
            //3.检查用户是否重复预约（同一个用户一天只能预约同一个套餐） 如果是重复预约则无法完成再次预约
            String  telephone = (String) map.get("telephone");//手机号
            Member member = memberDao.findByTelephone(telephone);
            if(member != null){   //会员
                //判断是否在重复预约
                Integer memberId = member.getId();  //会员id
                Date dateOrder = DateUtils.parseString2Date(orderDate); //预约日期
                String setmealId = (String) map.get("setmealId"); //套餐id
                Order order = new Order(memberId, dateOrder, Integer.parseInt(setmealId));
                //根据条件查询
                List<Order> orderList = this.orderDao.findByCondition(order);
                if(orderList != null && orderList.size() > 0){
                    //用户重复预约 无法完成再次预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }else{  //非会员
                //4 检查当前用户是否是会员 是会员直接完成预约 ，不是的话需要注册才能预约
                member = new Member();
                member.setName((String)map.get("name"));  //姓名
                member.setSex((String) map.get("sex")); //性别
                member.setPhoneNumber(telephone); //手机号呢
                member.setIdCard((String)map.get("idCard")); //身份证号
                member.setRegTime(new Date());  //注册时间
                memberDao.add(member);  //自动完成会员注册
            }

            //5.预约成功更新当前预约人数
            Order order = new Order();
            order.setMemberId(member.getId());  //会员id
            order.setOrderDate(DateUtils.parseString2Date(orderDate)); //预约日期
            order.setOrderType((String) map.get("orderType"));  //预约类型
            order.setOrderStatus(Order.ORDERSTATUS_NO);  //预约状态
            order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));  //套餐id
            orderDao.add(order);  //预约成功

            orderSetting.setReservations(orderSetting.getReservations()+1);  //可预约人数+1
            //更新当前预约人数
            orderSettingDao.editReservationsByOrderDate(orderSetting);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }
//根据预约id查询预约相关信息 （体检人姓名 预约日期  预约套餐名称  预约类型）
    @Override
    public Map findById(Integer id) throws Exception {//member order setmeal  三张表关联查询
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }




}

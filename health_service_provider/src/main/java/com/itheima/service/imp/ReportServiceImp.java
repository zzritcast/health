package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.ReportDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营数据统计服务
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImp implements ReportService {
    //综合
    @Autowired
    private ReportDao reportDao;
    
    //会员
    @Autowired
    private MemberDao memberDao;
    
    //预约
    @Autowired
    private OrderDao orderDao;
    
    
    //查询运营数据
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> result = new HashMap<>();
        //当前报表日期
        String tody = DateUtils.parseDate2String(DateUtils.getToday());
        //获得本周的第一天
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月的第一天
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        
        //本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(tody);//select * from t_member where regTime= tody
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount(); //select count(id) from t_member
        //本周新增会员
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);//select * from t_member where regTime>= thisWeekMonday
        //本月新增会员
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);////select * from t_member where regTime>= thisWeekMonday
        
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(tody);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(tody);//select count(id) from t_order where orderDate = #{value} and orderStatus = '已到诊'
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);//
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐查询
        List<Map> hotSetmeal = orderDao.findHotSetmeal();


        result.put("reportDate",tody);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}

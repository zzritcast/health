package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    //文件上传 实现预约设置数据批量导入
    @RequestMapping("/upload")
    @PreAuthorize("hasAuthority('ORDERSETTING')")  ////权限校验
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile); //使用POI解析表格数据
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                //获取日期
                String date = strings[0];
                //获取可预约数量
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(date),Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用服务是实现批量数据加入到数据库
            this.orderSettingService.add(data);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //解析失败
            return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    //根据月份查询对应的预约设置数据
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//date格式为：yyyy-MM
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    //根据日期设置对应的预约设置数据
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }


     /**
     * 分页查询
     *
     * @return
     */

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean pageBean) {
        try {
            PageInfo pageInfo = orderSettingService.findOrderPages(pageBean.getCurrentPage(), pageBean
                    .getPageSize(), pageBean.getQueryString());
            PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
    /* @PostMapping("/findPage")
     public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
         PageResult pageResult = this.orderSettingService.pageQuery(queryPageBean);
         return  pageResult;
     }*/

    //增加预约
    @PostMapping("/orderAdd")
    public Result orderAdd(@RequestBody Map order, Integer[] setmealIds) {
        try {
            this.orderSettingService.addOrder(order, setmealIds);
            return new Result(true, MessageConstant.ORDER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
//删除预约
    @DeleteMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.orderSettingService.deleteById(id);
            return new Result(true, MessageConstant.ORDER_CANCEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_CANCEL_FAIL);
        }
    }
//更新
    @PutMapping("/edit")
    public Result edit(Integer id) {
        try {
            this.orderSettingService.updateById(id);
            return new Result(true, MessageConstant.ORDER_EDIT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_EDIT_FAIL);
        }
    }
}

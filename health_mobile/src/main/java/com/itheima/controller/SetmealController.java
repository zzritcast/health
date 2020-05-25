package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐处理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetMealService setMealService;

    //查询所有套餐
    @GetMapping("/getAllSetmeal")
    public Result getAllSetmeal(){
        try{
            List<Setmeal> list = this.setMealService.findAll();
            return   new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @RequestMapping("/findBySetmealId")
    public Result findBySetmealId(int id){
        try{
            Setmeal setmeal = this.setMealService.findBySetmealId(id);
            return   new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //根据ID查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);//查询成功
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);//查询失败
        }
    }
}

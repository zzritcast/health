package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mem")
public class MemberController {


     @Reference
     private MemberService memberService;

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return this.memberService.pageQuery(queryPageBean);
    }

    //增加
    @PostMapping("/add")
    public Result orderAdd(@RequestBody Member member) {
        try {
            this.memberService.add(member);
            return new Result(true, MessageConstant.ADD_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_SUCCESS);
        }
    }

    //修改

    //更新
    @PutMapping("/edit")
    public Result edit(@RequestBody Member member) {
        try {
            this.memberService.updateById(member);
            return new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
    }

    //删除
    //删除预约
    @DeleteMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.memberService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }
    }
    //单个findById
    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
           Member member = this.memberService.findById(id);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS);
        }
    }
}

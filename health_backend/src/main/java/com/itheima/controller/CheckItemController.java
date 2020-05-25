package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*检查项管理*/
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference//查找服务
 private CheckItemService checkItemService;

    /*新增检查项*/
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")  ////权限校验
    public Result add(@RequestBody CheckItem checkItem){
        try{
           this.checkItemService.add(checkItem);
        }catch(Exception e){
            e.printStackTrace();
            //服务调用失败
            return  new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    //分页查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = this.checkItemService.pageQuery(queryPageBean);
       return  pageResult;
    }

    //删除
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")  ////权限校验
    public Result delete(Integer id){
        try{
            this.checkItemService.deleteById(id);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return   new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
    /*修改检查项*/
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")  ////权限校验
    public Result edit(@RequestBody CheckItem checkItem){
        try{
            this.checkItemService.update(checkItem);
        }catch(Exception e){
            e.printStackTrace();
            //服务调用失败
            return  new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //获取编辑id 的最新数据
    @GetMapping("/findById")
    public Result findById(Integer id){
        try{
           CheckItem checkItem = this.checkItemService.findById(id);
            return   new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //查询所有
    @GetMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")  ////权限校验
    public Result findAll(){
        try{
            List<CheckItem> list = this.checkItemService.findAll();
            return   new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}

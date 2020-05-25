package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查组管理
 */

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;
    //新增检查组
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")  ////权限校验
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try{
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);//新增失败
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);//新增成功
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.pageQuery(queryPageBean);
    }

    //根据ID查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);//查询成功
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);//查询失败
        }
    }

    //根据检查组ID查询检查组包含的多个检查项ID
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try{
            List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);//查询成功
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);//查询失败
        }
    }

    //编辑检查组
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")  ////权限校验
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try{
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);//新增失败
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);//新增成功
    }

    //删除
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")  ////权限校验
    public Result delete(Integer id){
        try{
            this.checkGroupService.deleteById(id);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

        return   new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有
    @GetMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")  ////权限校验
    public Result findAll(){
        try{
            List<CheckGroup> list = this.checkGroupService.findAll();
            return   new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}

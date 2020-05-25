package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName RoleController
 * @Author HETAO
 * @Date 2020/5/2 16:54
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;
    //查询所有

    @GetMapping("findAll")
    public List<Role> findAll() {
        return roleService.findAll();
    }

    //分页
    @PostMapping("/findPage")
    public PageInfo findPage(@RequestBody QueryPageBean pageBean) {
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<Role> list = roleService.findAll(pageBean.getQueryString());
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
    //角色增加
    @PutMapping("/roleAdd")
    public Result roleAdd(@RequestBody Role role, Integer[] permissionIds) {
        try {
            this.roleService.roleAdd(role, permissionIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FALL);
        }
    }
    //添加权限findById
    @PutMapping("/modifyPermission")
    public Result modifyPermission(Integer rid, @RequestBody Integer[] permissionIds) {
        try {
            this.roleService.modifyPermission(rid, permissionIds);
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }
    //删除权限findById
    @DeleteMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.roleService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FALL);
        }
    }



}

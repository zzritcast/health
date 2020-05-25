package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<Permission> list = permissionService.findAll();
            return  new Result(true, MessageConstant.GET_PERMISSION_SUCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false, MessageConstant.GET_PERMISSION_FALL);
        }
    }

    @GetMapping("/findById")
    public List<Permission> findById(Integer roleId) {
        List<Permission> list = permissionService.findByRId(roleId);
        return list;
    }
}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    //获取当前登录用户的用户名

    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring security 完成认证后，会将当前用户信息保存到框架提供的上下文对象中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if(user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);

        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);

    }
   /* @GetMapping("getMenuList")
    public Result getMenuList() {
        //获取当前用户信息
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            List<Menu> menuList = menuService.getMenu(details.getUsername());
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    //分页查询所有用户
    @PostMapping
    public PageResult findUserPage(@RequestBody QueryPageBean bean) {
        PageInfo<User> pageInfo = userService.findUserPage(bean.getCurrentPage(),
                bean.getPageSize(), bean.getQueryString());
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    //修改角色
    @PostMapping("edit")
    public Result editUserRole(@RequestParam("id") String id, @RequestBody String[] roles) {
        try {
            this.userService.modifyRole(id, roles);
            return new Result(true, MessageConstant.EDIT_USER_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_ROLE_FALL);
        }
    }

    //获取用户对应的角色
    @GetMapping()
    public List<Role> findRoleByUser(@RequestParam("id") String id) {
        List<Role> list = this.roleService.findRoleByUid(id);
        return list;
    }*/
}

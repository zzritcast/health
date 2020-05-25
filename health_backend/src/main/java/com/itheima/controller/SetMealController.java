package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 体检套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetMealService setMealService;

    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        System.out.println(imgFile);
        String originalFilename = imgFile.getOriginalFilename();//原始文件名03a36073-a140-4942-9b9b-712cecb144901.jpg
        String end = originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg
        String fileName = UUID.randomUUID().toString() + end; // FuM1Sa5TtL_ekLsdkYWcf5pyjKGu.jpg
        try {
            //将文件上传到七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //将上传图片名称存入redis，基于redis的set集合存储所有的上传图片
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return  new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    //新增检查组
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")  ////权限校验
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try{
            this.setMealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);//新增失败
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);//新增成功
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return this.setMealService.pageQuery(queryPageBean);
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

    //根据检查组ID查询检查组包含的多个检查项ID
    @RequestMapping("/findCheckGroupIdsBySetMealId")
    public Result findCheckGroupIdsBySetMealId(Integer id){
        try{
            List<Integer> checkitemIds = setMealService.findCheckGroupIdsBySetMealId(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,checkitemIds);//查询成功
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);//查询失败
        }
    }

    //编辑检查组
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")  ////权限校验
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try{
            this.setMealService.edit(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);//新增失败
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);//新增成功
    }
    //删除
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")  ////权限校验
    public Result delete(Integer id){
        try{
            this.setMealService.deleteById(id);
        }catch(Exception e){
            e.printStackTrace();
            //服务器调用失败
            return new Result(false,MessageConstant.DELETE_SETMEAL_FALL);
        }

        return   new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
    @GetMapping("/findAll")
    public Result findAll() {
        try {
            List<Setmeal> list = this.setMealService.findAll();
            System.out.println(list);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }


}

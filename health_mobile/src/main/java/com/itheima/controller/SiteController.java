package com.itheima.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.service.SiteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SiteController
 * @Author HETAO
 * @Date 2020/5/3 8:33
 */
@RestController
@RequestMapping("/site")
public class SiteController {
    @Reference
    private SiteService siteService;

    /**
     * 分页查询
     *
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean bean) {
        try {
            PageInfo page = siteService.findByPage(bean.getCurrentPage(), bean.getPageSize(), bean.getQueryString());
            PageResult pageResult = new PageResult(page.getTotal(), page.getList());
            return new Result(true, MessageConstant.QUERY_ADDRESS_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ADDRESS_FAIL);
        }
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.siteService.delteAddressById(id);
            return new Result(true, MessageConstant.SITE_DELETE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SITE_DELETE_FAIL);
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody Address address) {
        try {
            this.siteService.save(address);
            return new Result(true, MessageConstant.SITE_SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SITE_SAVE_FAIL);
        }
    }

    @GetMapping("/all")
    public List<Address> all() {
        return this.siteService.findAll();
    }
}

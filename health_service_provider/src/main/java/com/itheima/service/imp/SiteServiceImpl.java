package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.SiteDao;
import com.itheima.pojo.Address;
import com.itheima.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName SiteServiceImpl
 * @Author HETAO
 * @Date 2020/5/3 8:36
 */
@Service(interfaceClass = SiteService.class)
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteDao siteMapper;

    @Override
    public PageInfo findByPage(Integer currentPage, Integer pageSize, String queryString) {
        if (currentPage == null || currentPage < 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Address> list = siteMapper.findAll(queryString);
        return new PageInfo(list);
    }

    @Override
    public void delteAddressById(Integer id) {
        this.siteMapper.deleteById(id);
    }

    @Override
    public void save(Address address) {
        address.setId(null);
        this.siteMapper.add(address);
    }

    @Override
    public List<Address> findAll() {
        return this.siteMapper.findAll(null);
    }
}

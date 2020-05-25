package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Address;

import java.util.List;

/**
 * @InterfaceName SiteService
 * @Author HETAO
 * @Date 2020/5/3 8:36
 */
public interface SiteService {
    PageInfo findByPage(Integer currentPage, Integer pageSize, String queryString);

    void delteAddressById(Integer id);

    void save(Address address);

    List<Address> findAll();
}

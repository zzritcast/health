package com.itheima.dao;

import com.itheima.pojo.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName SiteMapper
 * @Author HETAO
 * @Date 2020/5/3 8:42
 */
public interface SiteDao {
    List<Address> findAll(@Param("condition") String queryString);

    void deleteById(Integer id);

    void add(Address address);
}

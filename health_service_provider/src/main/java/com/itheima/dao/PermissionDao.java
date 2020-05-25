package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);

    List<Permission> findByRid(Integer id);

    List<Permission> findAll();

}

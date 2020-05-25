package com.itheima.service;

import com.itheima.pojo.Permission;

import java.util.List;

public interface PermissionService {
    public List<Permission> findAll();

    List<Permission> findByRId(Integer roleId);
}

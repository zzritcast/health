package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceimp implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findAll() {
        List<Permission> list = permissionDao.findAll();
        return list;
    }

    @Override
    public List<Permission> findByRId(Integer roleId) {
        List<Permission> list = this.permissionDao.findByRid(roleId);
        return list;
    }
}

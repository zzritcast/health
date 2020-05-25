package com.itheima.service;



import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;

/**
 * @InterfaceName RoleService
 * @Author HETAO
 * @Date 2020/5/2 16:55
 */
public interface RoleService {
    List<Role> findAll();

    List<Role> findRoleByUid(String id);

    void modifyPermission(Integer rId,Integer[] permissionIds);

    List<Role> findAll(String queryString);

    void roleAdd(Role role, Integer[] permissionIds);

    void deleteById(Integer id);
}

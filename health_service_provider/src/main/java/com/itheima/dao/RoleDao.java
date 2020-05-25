package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    
    
    
    List<Role> findRoleByUid(Integer id);

    List<Role> findAll();

    void deletePermissionByRid(Integer rid);

    void insertPermissIds(Integer rid, Integer[] permissionIds);

    List<Role> findAllByCondition(@Param("condition") String queryString);

    Set<Role> findByUserId(Integer userId);
    //加入角色

    void roleAdd(Role role);
    //中间表

    void addRoleAndPer(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    void deleteById(Integer id);
}

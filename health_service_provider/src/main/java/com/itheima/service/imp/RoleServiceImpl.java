package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;

import com.itheima.dao.RoleDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName RoleServiceImpl
 * @Author HETAO
 * @Date 2020/5/2 16:56
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleDao roleMapper;

    @Override
    public List<Role> findAll() {
        return this.roleMapper.findAll();
    }

    @Override
    public List<Role> findRoleByUid(String id) {
        return roleMapper.findRoleByUid(Integer.parseInt(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPermission(Integer rId, Integer[] permissionIds) {
        this.roleMapper.deletePermissionByRid(rId);
        if(permissionIds != null && permissionIds.length >0){
            for (Integer permissionId : permissionIds) {
                this.roleMapper.addRoleAndPer(rId,permissionId);
            }
        }

    }

    @Override
    public List<Role> findAll(String queryString) {
        return this.roleMapper.findAllByCondition(queryString);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleAdd(Role role, Integer[] permissionIds) {
        this.roleMapper.roleAdd(role);
        Integer roleId = role.getId();
        if(permissionIds != null && permissionIds.length >0){
            for (Integer permissionId: permissionIds) {
                this.roleMapper.addRoleAndPer(roleId,permissionId);
            }
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        //删掉中间表的id
        this.roleMapper.deletePermissionByRid(id);
        //删除角色中的id
        this.roleMapper.deleteById(id);
    }

}

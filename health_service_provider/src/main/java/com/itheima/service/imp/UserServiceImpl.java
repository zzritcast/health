package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

/**
 * 用户服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息，不包含用户的角色
        if(user == null){
            return  null;
        }
        Integer userId = user.getId();
        //根据用户ID查询对应的角色
        Set<Role> roleSet = roleDao.findByUserId(userId);
        if(roleSet!= null && roleSet.size() > 0){
            for (Role role : roleSet) {
                Integer roleId = role.getId();
                //根据角色ID查询关联的权限
                Set<Permission> permissionSet = permissionDao.findByRoleId(roleId);
                if(permissionSet != null && permissionSet.size() > 0){
                    role.setPermissions(permissionSet);//让角色关联权限
                }
            }
        }
        user.setRoles(roleSet);//让用户关联角色
        return user;
    }
}

package com.framework.auth.service.impl;

import com.framework.auth.mapper.*;
import com.framework.auth.pojo.entity.*;
import com.framework.auth.service.SecurityService;
import com.framework.security.model.Permission;
import com.framework.security.model.Role;
import com.framework.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SecurityServiceImpl
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: xuaj
 * @date: 2017年11月23日 上午10:25:11
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private PermissionEntityMapper permissionEntityMapper;

    @Autowired
    private RoleEntityMapper roleEntityMapper;
    @Autowired
    private RolePermissionEntityMapper rolePermissionEntityMapper;
    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserRoleEntityMapper userRoleEntityMapper;

    @Autowired
    private SecurityDao securityDao;

    @Override
    public boolean addRolePermission(RoleEntity role,
                                     PermissionEntity permission) {
        if (role.getId() == 0) {
            roleEntityMapper.insertSelective(role);
        }
        if (permission.getId() == 0) {
            permissionEntityMapper.insertSelective(permission);
        }

        RolePermissionEntity record = new RolePermissionEntity();
        if (permission.getId() != 0 && role.getId() != 0) {
            record.setCreatedDatetime(new Date());
            record.setPermissionId(permission.getId());
            record.setRoleId(role.getId());
            return rolePermissionEntityMapper.insertSelective(record) > 0;
        } else {
            return false;
        }

    }


    @Override
    public boolean delRolePermission(Integer permissionId, Integer roleId) {
        RolePermissionEntity record = new RolePermissionEntity();
        //record.setCreatedDatetime(new Date());
        record.setPermissionId(permissionId);
        record.setRoleId(roleId);
        return rolePermissionEntityMapper.deleteByExample(record) > 0;
    }


    @Override
    public List<Permission> queryPermissionByRoleId(List<Integer> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        RolePermissionEntityExample rolePermissionEntityExample = new RolePermissionEntityExample();
        rolePermissionEntityExample.createCriteria().andRoleIdIn(ids);
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionEntityMapper.selectByExample(rolePermissionEntityExample);
        /*提取权限id*/
        List<Integer> permissionIds = rolePermissionEntities.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());

        PermissionEntityExample example = new PermissionEntityExample();
        example.createCriteria().andIdIn(permissionIds);
        List<PermissionEntity> permissionEntities = permissionEntityMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(permissionEntities)) {
            return Collections.emptyList();
        }

        List<Permission> list = new LinkedList<>();
        /*建立索引*/
//        Map<Integer, Permission> index = new HashMap<>(permissionEntities.size());
//        Permission root = new Permission();

        for (PermissionEntity permissionEntity : permissionEntities) {
            Permission permission = new Permission();
            permission.setCache(permissionEntity.getCacheable());
            permission.setIcon(permissionEntity.getIcon());
            permission.setId(permissionEntity.getId());
            permission.setMenuType(permissionEntity.getMenuType());
            permission.setName(permissionEntity.getPermission());
            permission.setOrderNum(permissionEntity.getOrderNum());
            permission.setpDesc(permissionEntity.getDescription());
            permission.setPerm(permissionEntity.getPerm());
            permission.setUri(permissionEntity.getUrl());
            permission.setVisible(permissionEntity.getVisible());
//            if (permissionEntity.getParentId() == null) {
//                permission.setParent(root);
//            }
            list.add(permission);
//            index.put(permission.getId(), permission);
        }

//        /*关联父权限*/
//        for (PermissionEntity permissionEntity : permissionEntities) {
//            if (permissionEntity.getParentId() == null) {
//                continue;
//            }
//            Permission permission = index.get(permissionEntity.getId());
//            Permission parentPermission = index.get(permissionEntity.getParentId());
//            permission.setParent(parentPermission);
//        }
//        /*构建权限树*/
//        ConcurrentMap<Permission, List<Permission>> map = list.stream().collect(Collectors.groupingByConcurrent(Permission::getParent));
//        for (Permission permission : list) {
//            List<Permission> children = map.get(permission);
//            if (!CollectionUtils.isEmpty(children)) {
//                continue;
//            }
//            permission.setChildren(children);
//        }
//        list = map.get(root);

//		List<Permission> list=new ArrayList<Permission>();
//		if(!plist.isEmpty()){
//			for (PermissionEntity p:plist) {
//				 Permission permission =new Permission();
//				 permission.setId(p.getId());
//				 permission.setName(p.getPermission());
//				 permission.setUri(p.getUrl());
//				// BeanUtils.copyProperties(r,permission);
//				 list.add(permission);
//				 
//			}
//		}
        return list;
    }


    @Override
    public List<Role> queryRolesByUserId(Integer userId) {
        List<Role> list = securityDao.selectRolesByUserId(userId);
//		List<Role> list=new ArrayList<Role>();
//		if(!rlist.isEmpty()){
//			for (RoleEntity r:rlist) {
//				 Role role = new Role();
//				 role.setId(r.getId());
//				 role.setName(r.getName());
//				 role.setRoleCode(r.getName());
//				 role.setType(r.getDescription());
//				 role.setStatus(r.getDeleteFlag());
//				// BeanUtils.copyProperties(r,role);	
//				 list.add(role);
//			}
//		}
        return list;
    }


    @Override
    public User findUserByUserNum(String username) {
        UserEntityExample example = new UserEntityExample();
        example.createCriteria().andUserNameEqualTo(username);
        List<UserEntity> list = userEntityMapper.selectByExample(example);
        User res = null;
        if (!list.isEmpty()) {
            res = new User();
            res.setId(list.get(0).getId());
            res.setUserName(list.get(0).getUserName());
            res.setPwd(list.get(0).getPwd());
            res.setNickName(list.get(0).getNickName());
            res.setStatus(list.get(0).getDeleteFlag() ? 0 : 1);
        }
        return res;
    }


    @Override
    public boolean addUserRole(Integer userId, Integer roleId) {
        UserRoleEntity record = new UserRoleEntity();
        record.setUserId(userId);
        record.setRoleId(roleId);
        return userRoleEntityMapper.insertSelective(record) > 0;
    }


    @Override
    public boolean delUserRole(Integer userId, Integer roleId) {
        UserRoleEntityExample example = new UserRoleEntityExample();
        example.createCriteria().andUserIdEqualTo(userId).andRoleIdEqualTo(roleId);
        return userRoleEntityMapper.deleteByExample(example) > 0;
    }

}
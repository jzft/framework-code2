package com.framework.auth.service.impl;

import com.framework.auth.mapper.RolePermissionEntityMapper;
import com.framework.auth.pojo.entity.RolePermissionEntity;
import com.framework.auth.pojo.entity.RolePermissionEntityExample;
import com.framework.auth.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2020/6/19 下午4:30
 *
 * @author casper
 **/
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionEntityMapper rolePermissionEntityMapper;


    /**
     * 批量保存
     *
     * @param target 数据
     * @return 数据的id集合
     */
    @Override
    public List<Integer> saveBatch(List<RolePermissionEntity> target) {
        if (CollectionUtils.isEmpty(target)) {
            return Collections.emptyList();
        }
        for (RolePermissionEntity entity : target) {
            rolePermissionEntityMapper.insertSelective(entity);
        }
        return target.stream().map(RolePermissionEntity::getId).collect(Collectors.toList());
    }

    /**
     * 通过角色id清空角色和权限的关系
     *
     * @param roleId 角色id
     */
    @Override
    @Transactional
    public void clearByRoleId(Integer roleId) {
        RolePermissionEntityExample example = new RolePermissionEntityExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionEntityMapper.deleteByExample(example);
    }

    /**
     * 通过角色id获取角色权限关联关系
     *
     * @param roleId 角色id
     * @return 关联关系列表
     */
    @Override
    public List<RolePermissionEntity> getByRoleId(Integer roleId) {
        RolePermissionEntityExample example = new RolePermissionEntityExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return rolePermissionEntityMapper.selectByExample(example);
    }

    /**
     * 根据权限id查询角色权限关联信息
     *
     * @param permissionId 权限id
     * @return
     */
    @Override
    public List<RolePermissionEntity> getByPermissionId(Integer permissionId) {
        RolePermissionEntityExample example = new RolePermissionEntityExample();
        example.createCriteria().andPermissionIdEqualTo(permissionId);
        return rolePermissionEntityMapper.selectByExample(example);
    }
}

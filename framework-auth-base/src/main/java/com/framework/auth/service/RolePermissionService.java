package com.framework.auth.service;


import com.framework.auth.pojo.entity.RolePermissionEntity;

import java.util.List;

/**
 * @author casper
 * @date 2020/6/19 下午4:25
 **/
public interface RolePermissionService {


    /**
     * 批量保存
     *
     * @param target 数据
     * @return 数据的id集合
     */
    List<Integer> saveBatch(List<RolePermissionEntity> target);

    /**
     * 通过角色id清空角色和权限的关系
     *
     * @param roleId 角色id
     */
    void clearByRoleId(Integer roleId);

    /**
     * 通过角色id获取角色权限关联关系
     *
     * @param roleId 角色id
     * @return 关联关系列表
     */
    List<RolePermissionEntity> getByRoleId(Integer roleId);

    /**
     * 根据权限id查询角色权限关联信息
     *
     * @param permissionId 权限id
     * @return
     */
    List<RolePermissionEntity> getByPermissionId(Integer permissionId);


}

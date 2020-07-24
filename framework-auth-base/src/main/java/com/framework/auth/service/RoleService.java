package com.framework.auth.service;


import com.framework.auth.pojo.dto.role.RoleInfoDTO;
import com.framework.auth.pojo.dto.role.RolePermissionInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;

import java.util.Collection;
import java.util.List;

/**
 * date 2020/6/19 上午11:24
 *
 * @author casper
 **/
public interface RoleService {


    /**
     * 保存或者更新角色信息
     * 不包含参数校验
     *
     * @param roleInfoDTO 角色信息
     * @return 角色id
     */
    Integer saveOrUpdate(RoleInfoDTO roleInfoDTO);


    /**
     * 获取角色信息
     *
     * @param id 角色id
     * @return 角色信息，不存在则返回null
     */
    RoleEntity getById(Integer id);

    /**
     * 通过角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    RoleEntity getByRoleName(String roleName);

    /**
     * 重新绑定权限，旧权限会清除
     *
     * @param rolePermissionInfoDTO 角色和权限关系信息
     */
    void rebindingPermissions(RolePermissionInfoDTO rolePermissionInfoDTO);

    /**
     * 批量查询指定id的角色
     *
     * @param ids 角色id
     * @return
     */
    List<RoleEntity> getByRoleIds(List<Integer> ids);


    /**
     * 更新角色的有效状态
     *
     * @param entity    角色实体信息
     * @param available 是否有效
     */
    void updateAvailableStatus(RoleEntity entity, boolean available);


}

package com.framework.auth.service.impl;

import com.framework.auth.mapper.RoleEntityMapper;
import com.framework.auth.pojo.dto.role.RoleInfoDTO;
import com.framework.auth.pojo.dto.role.RolePermissionInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.RoleEntityExample;
import com.framework.auth.pojo.entity.RolePermissionEntity;
import com.framework.auth.service.RolePermissionService;
import com.framework.auth.service.RoleService;
import com.framework.cache.RedisHelper;
import com.framework.security.SecurityFilterUtil;
import com.framework.auth.util.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * date 2020/6/19 下午4:39
 *
 * @author casper
 **/
@Service
public class RoleServiceImpl implements RoleService {


    /**
     * 通用超时时间
     */
    private final int expireTime = 5000;

    @Autowired
    private RoleEntityMapper roleEntityMapper;
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 保存或者更新角色信息
     * 不包含参数校验
     * 使用前先校验角色名称有没有相同的已存在数据库
     *
     * @param roleInfoDTO 角色信息
     * @return 角色id
     */
    @Override
    public Integer saveOrUpdate(RoleInfoDTO roleInfoDTO) {
        return RedisHelper.ignoreExec(() -> {
            RoleEntity entity = new RoleEntity();
            entity.setDescription(roleInfoDTO.getDesc());
            entity.setName(roleInfoDTO.getRoleName());
            entity.setId(roleInfoDTO.getId());
            entity.setRoleCode(roleInfoDTO.getRoleCode());
            if (entity.getId() == null) {
                roleEntityMapper.insert(entity);
            } else {
                roleEntityMapper.updateByPrimaryKey(entity);
            }
            return entity.getId();
        }, KeyConstants.ACTION_ROLE_MODIFY_KEY + roleInfoDTO.getRoleName(), expireTime);
    }

    /**
     * 获取角色信息
     *
     * @param id 角色id
     * @return 角色信息，不存在则返回null
     */
    @Override
    public RoleEntity getById(Integer id) {
        return roleEntityMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    @Override
    public RoleEntity getByRoleName(String roleName) {
        RoleEntityExample example = new RoleEntityExample();
        example.createCriteria().andNameEqualTo(roleName);
        List<RoleEntity> roleEntities = roleEntityMapper.selectByExample(example);
        return CollectionUtils.isEmpty(roleEntities) ? null : roleEntities.get(0);
    }

    /**
     * 重新绑定权限，旧权限会清除
     *
     * @param rolePermissionInfoDTO 角色和权限关系信息
     */
    @Override
    @Transactional
    public void rebindingPermissions(RolePermissionInfoDTO rolePermissionInfoDTO) {

        RoleEntity roleEntity = roleEntityMapper.selectByPrimaryKey(rolePermissionInfoDTO.getRoleId());
        if (roleEntity == null) {
            throw new RuntimeException("角色不存在");
        }
        RedisHelper.ignoreExec(() -> {
            /*需要操作数据库的和缓存的两部分数据*/
            /*处理缓存，清空缓存*/
            SecurityFilterUtil.delFilterRoleUri(roleEntity.getRoleCode());
            /*处理数据库*/
            rolePermissionService.clearByRoleId(rolePermissionInfoDTO.getRoleId());
            List<Integer> permissionIds = rolePermissionInfoDTO.getPermissionId();
            if (!CollectionUtils.isEmpty(permissionIds)) {
                List<RolePermissionEntity> target = new ArrayList<>(permissionIds.size());
                for (Integer permissionId : permissionIds) {
                    RolePermissionEntity entity = new RolePermissionEntity();
                    entity.setPermissionId(permissionId);
                    entity.setRoleId(rolePermissionInfoDTO.getRoleId());
                    target.add(entity);
                }
                rolePermissionService.saveBatch(target);
            }
            return null;
        }, KeyConstants.ACTION_USER_ROLE_BIND_KEY + rolePermissionInfoDTO.getRoleId(), expireTime);
    }

    /**
     * 批量查询指定id的角色
     *
     * @param ids 角色id
     * @return
     */
    @Override
    public List<RoleEntity> getByRoleIds(List<Integer> ids) {
        RoleEntityExample example = new RoleEntityExample();
        example.createCriteria().andIdIn(ids);
        return roleEntityMapper.selectByExample(example);
    }

    /**
     * 更新角色的有效状态
     *
     * @param entity    角色实体信息
     * @param available 是否有效
     */
    @Override
    public void updateAvailableStatus(RoleEntity entity, boolean available) {
        if (!entity.getAvailable().equals(available)) {
            entity.setAvailable(available);
            roleEntityMapper.updateByPrimaryKey(entity);
        }
    }


}

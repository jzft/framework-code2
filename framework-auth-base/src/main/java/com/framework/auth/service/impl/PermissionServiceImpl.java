package com.framework.auth.service.impl;

import com.framework.auth.mapper.PermissionEntityMapper;
import com.framework.auth.pojo.dto.permission.PermissionInfoDTO;
import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.PermissionEntityExample;
import com.framework.auth.service.PermissionService;
import com.framework.auth.util.KeyConstants;
import com.framework.cache.RedisHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * date 2020/6/19 下午5:48
 *
 * @author casper
 **/
@Service
public class PermissionServiceImpl implements PermissionService {

    /**
     * 通用超时时间
     */
    private final int expireTime = 5000;

    @Autowired
    private PermissionEntityMapper permissionEntityMapper;


    /**
     * 保存或者更新权限
     * 不包括参数校验
     *
     * @param permissionInfoDTO 权限信息
     * @return 权限id
     */
    @Override
    public Integer saveOrUpdate(PermissionInfoDTO permissionInfoDTO) {
        return RedisHelper.ignoreExec(() -> {
            PermissionEntity entity = new PermissionEntity();
            entity.setAvailable(permissionInfoDTO.isAvailable());
            entity.setCacheable(permissionInfoDTO.isAvailable());
            entity.setDescription(permissionInfoDTO.getDescription());
            entity.setIcon(permissionInfoDTO.getIcon());
            entity.setMenuType(permissionInfoDTO.getMenuType());
            entity.setOrderNum(permissionInfoDTO.getOrderNum());
            entity.setParentId(permissionInfoDTO.getParentId());
            entity.setPerm(permissionInfoDTO.getPerm());
            entity.setPermission(permissionInfoDTO.getPermission());
            entity.setSysType(permissionInfoDTO.getSysType());
            entity.setUrl(permissionInfoDTO.getUrl());
            entity.setVisible(permissionInfoDTO.isVisible());
            if (permissionInfoDTO.getId() != null) {
                entity.setId(permissionInfoDTO.getId());
                permissionEntityMapper.updateByPrimaryKeySelective(entity);
            } else {
                permissionEntityMapper.insertSelective(entity);
            }
            return entity.getId();
        }, KeyConstants.ACTION_PERMISSION_MODIFY_KEY + permissionInfoDTO.getPermission(), expireTime);
    }

    /**
     * 根据id查询权限信息
     *
     * @param id 权限id
     * @return 权限信息，查询不到返回null
     */
    @Override
    public PermissionEntity getById(Integer id) {
        return permissionEntityMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询权限信息
     *
     * @param example 查询条件
     * @param index   页号
     * @param size    页大小
     */
    @Override
    public PageInfo<PermissionEntity> getByPage(PermissionEntityExample example, int index, int size) {
        PageHelper.startPage(index, size);
        List<PermissionEntity> permissionEntities = permissionEntityMapper.selectByExample(example);
        return new PageInfo<>(permissionEntities);
    }

    /**
     * 查询列表
     *
     * @param example 查询条件
     * @return
     */
    @Override
    public List<PermissionEntity> getListByExample(PermissionEntityExample example) {
        return permissionEntityMapper.selectByExample(example);
    }
}

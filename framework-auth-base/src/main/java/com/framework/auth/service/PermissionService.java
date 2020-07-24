package com.framework.auth.service;

import com.framework.auth.pojo.dto.permission.PermissionInfoDTO;
import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.PermissionEntityExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author casper
 * @date 2020/6/19 上午11:36
 **/
public interface PermissionService {


    /**
     * 保存或者更新权限
     *
     * @param permissionInfoDTO 权限信息
     * @return 权限id
     */
    Integer saveOrUpdate(PermissionInfoDTO permissionInfoDTO);

    /**
     * 根据id查询权限信息
     *
     * @param id 权限id
     * @return 权限信息，查询不到返回null
     */
    PermissionEntity getById(Integer id);

    /**
     * 分页查询权限信息
     *
     * @param example 查询条件
     * @param index   页号
     * @param size    页大小
     */
    PageInfo<PermissionEntity> getByPage(PermissionEntityExample example, int index, int size);


    /**
     * 查询列表
     *
     * @param example 查询条件
     * @return
     */
    List<PermissionEntity> getListByExample(PermissionEntityExample example);


}

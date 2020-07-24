package com.framework.auth.service;


import com.framework.auth.pojo.entity.UserRoleEntity;

import java.util.List;

/**
 * @author casper
 * @date 2020/6/19 下午2:18
 **/
public interface UserRoleService {


    /**
     * 查询指定用户关联的角色id
     *
     * @param userId 用户id
     * @return
     */
    List<UserRoleEntity> getByUserId(Integer userId);

    /**
     * 批量保存到数据库
     *
     * @param target 数据
     */
    List<Integer> saveBatch(List<UserRoleEntity> target);

    /**
     * 清除指定用户的角色关联关系
     *
     * @param userId 用户id
     */
    void clearByUserId(Integer userId);

}

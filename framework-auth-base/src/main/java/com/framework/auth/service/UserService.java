package com.framework.auth.service;



import com.framework.auth.pojo.dto.user.UserInfoDTO;
import com.framework.auth.pojo.dto.user.UserRoleInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.UserEntity;

import java.util.List;

public interface UserService {

    /**
     * 保存或者更新用户信息
     * 不包含参数校验
     *
     * @param userInfo 用户信息
     * @return 用户的id
     */
    Integer saveOrUpdate(UserInfoDTO userInfo);

    /**
     * 通过id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    UserEntity getById(Integer id);


    /**
     * 重新绑定角色
     *
     * @param userRoleInfoDTO 用户角色信息
     */
    void rebindingRoles(UserRoleInfoDTO userRoleInfoDTO);

    /**
     * 获取指定用户的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<RoleEntity> getUserRoles(Integer userId);

}

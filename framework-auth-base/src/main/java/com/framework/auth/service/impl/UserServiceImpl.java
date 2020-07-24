package com.framework.auth.service.impl;


import com.framework.auth.mapper.RoleEntityMapper;
import com.framework.auth.mapper.UserEntityMapper;
import com.framework.auth.pojo.dto.user.UserInfoDTO;
import com.framework.auth.pojo.dto.user.UserRoleInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.RoleEntityExample;
import com.framework.auth.pojo.entity.UserEntity;
import com.framework.auth.pojo.entity.UserRoleEntity;
import com.framework.auth.service.UserRoleService;
import com.framework.auth.service.UserService;
import com.framework.cache.RedisHelper;
import com.framework.security.AESHashedMatcher;
import com.framework.auth.util.KeyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 通用操作超时时间，单位秒
     */
    private final int expireTime = 5000;


    /**
     * 加密器
     */
    @Autowired
    private AESHashedMatcher hashedCredentialsMatcher;
    /**
     * 用户信息持久层
     */
    @Autowired
    private UserEntityMapper userEntityMapper;
    /**
     * 角色持久层
     */
    @Autowired
    private RoleEntityMapper roleEntityMapper;

    /**
     * 用户角色关系持久层
     */
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 保存或者更新用户信息
     * 不包含参数校验
     *
     * @param userInfo 用户信息
     * @return 用户的id
     */
    @Override
    @Transactional
    public Integer saveOrUpdate(UserInfoDTO userInfo) {
        return RedisHelper.ignoreExec(() -> {
            UserEntity entity = new UserEntity();
            entity.setNickName(userInfo.getNickName());
            entity.setId(userInfo.getId());
            entity.setUserName(userInfo.getUsername());
            entity.setParentId(userInfo.getParentId());
            entity.setType(userInfo.getType());
            if (!StringUtils.isEmpty(userInfo.getPassword())) {
                try {
                    String encryptPwd = hashedCredentialsMatcher.aesEncrypt(userInfo.getPassword(), hashedCredentialsMatcher.getEncryptKey());
                    entity.setPwd(encryptPwd);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (entity.getId() != null) {
                userEntityMapper.updateByPrimaryKey(entity);
            } else {
                userEntityMapper.insertSelective(entity);
            }
            return entity.getId();
        }, KeyConstants.ACTION_USER_MODIFY_KEY + userInfo.getUsername(), expireTime);
    }

    /**
     * 通过id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public UserEntity getById(Integer id) {
        return userEntityMapper.selectByPrimaryKey(id);
    }

    /**
     * 重新绑定角色
     *
     * @param userRoleInfoDTO 用户角色信息
     */
    @Override
    @Transactional
    public void rebindingRoles(UserRoleInfoDTO userRoleInfoDTO) {
        RedisHelper.ignoreExec(() -> {
            /*清除旧的数据*/
            userRoleService.clearByUserId(userRoleInfoDTO.getUserId());
            /*添加新数据*/
            List<UserRoleEntity> result = new LinkedList<>();
            for (Integer roleId : userRoleInfoDTO.getRoleIds()) {
                UserRoleEntity entity = new UserRoleEntity();
                entity.setRoleId(roleId);
                entity.setUserId(userRoleInfoDTO.getUserId());
                result.add(entity);
            }
            userRoleService.saveBatch(result);
            return null;
        }, KeyConstants.ACTION_USER_ROLE_BIND_KEY + userRoleInfoDTO.getUserId(), expireTime);
    }

    /**
     * 获取指定用户的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<RoleEntity> getUserRoles(Integer userId) {
        List<UserRoleEntity> userRoleEntityList = userRoleService.getByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return Collections.emptyList();
        }
        List<Integer> roleIds = userRoleEntityList.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        RoleEntityExample example = new RoleEntityExample();
        example.createCriteria().andIdIn(roleIds);
        return roleEntityMapper.selectByExample(example);
    }
}

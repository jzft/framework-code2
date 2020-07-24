package com.framework.auth.service.impl;

import com.framework.auth.mapper.UserRoleEntityMapper;
import com.framework.auth.pojo.entity.UserRoleEntity;
import com.framework.auth.pojo.entity.UserRoleEntityExample;
import com.framework.auth.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2020/6/19 下午3:45
 *
 * @author casper
 **/
@Service
public class UserRoleServiceImpl implements UserRoleService {


    @Autowired
    private UserRoleEntityMapper userRoleEntityMapper;


    /**
     * 查询指定用户关联的角色id
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserRoleEntity> getByUserId(Integer userId) {
        UserRoleEntityExample entityExample = new UserRoleEntityExample();
        entityExample.createCriteria().andUserIdEqualTo(userId);
        return userRoleEntityMapper.selectByExample(entityExample);
    }

    /**
     * 批量保存到数据库
     *
     * @param target 数据
     */
    @Override
    @Transactional
    public List<Integer> saveBatch(List<UserRoleEntity> target) {
        if (CollectionUtils.isEmpty(target)) {
            return Collections.emptyList();
        }
        for (UserRoleEntity entity : target) {
            userRoleEntityMapper.insert(entity);
        }
        return target.stream().map(UserRoleEntity::getId).collect(Collectors.toList());
    }

    /**
     * 清除指定用户的角色关联关系
     *
     * @param userId 用户id
     */
    @Override
    @Transactional
    public void clearByUserId(Integer userId) {
        UserRoleEntityExample example = new UserRoleEntityExample();
        example.createCriteria().andUserIdEqualTo(userId);
        userRoleEntityMapper.deleteByExample(example);
    }
}

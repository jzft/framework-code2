package com.framework.auth.mapper;

import com.framework.auth.pojo.entity.UserRoleEntity;
import com.framework.auth.pojo.entity.UserRoleEntityExample;
import com.framework.dao.base.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserRoleEntityMapper extends BaseMapper {
    int countByExample(UserRoleEntityExample example);

    int deleteByExample(UserRoleEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleEntity record);

    int insertSelective(UserRoleEntity record);

    List<UserRoleEntity> selectByExampleWithRowbounds(UserRoleEntityExample example, RowBounds rowBounds);

    List<UserRoleEntity> selectByExample(UserRoleEntityExample example);

    UserRoleEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRoleEntity record, @Param("example") UserRoleEntityExample example);

    int updateByExample(@Param("record") UserRoleEntity record, @Param("example") UserRoleEntityExample example);

    int updateByPrimaryKeySelective(UserRoleEntity record);

    int updateByPrimaryKey(UserRoleEntity record);
}
package com.framework.auth.mapper;

import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.RoleEntityExample;
import com.framework.dao.base.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface RoleEntityMapper extends BaseMapper {
    int countByExample(RoleEntityExample example);

    int deleteByExample(RoleEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleEntity record);

    int insertSelective(RoleEntity record);

    List<RoleEntity> selectByExampleWithRowbounds(RoleEntityExample example, RowBounds rowBounds);

    List<RoleEntity> selectByExample(RoleEntityExample example);

    RoleEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleEntity record, @Param("example") RoleEntityExample example);

    int updateByExample(@Param("record") RoleEntity record, @Param("example") RoleEntityExample example);

    int updateByPrimaryKeySelective(RoleEntity record);

    int updateByPrimaryKey(RoleEntity record);
}
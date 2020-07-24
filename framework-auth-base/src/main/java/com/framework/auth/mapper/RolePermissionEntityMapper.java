package com.framework.auth.mapper;

import com.framework.auth.pojo.entity.RolePermissionEntity;
import com.framework.auth.pojo.entity.RolePermissionEntityExample;
import com.framework.dao.base.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface RolePermissionEntityMapper extends BaseMapper {
    int countByExample(RolePermissionEntityExample example);

    int deleteByExample(RolePermissionEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RolePermissionEntity record);

    int insertSelective(RolePermissionEntity record);

    List<RolePermissionEntity> selectByExampleWithRowbounds(RolePermissionEntityExample example, RowBounds rowBounds);

    List<RolePermissionEntity> selectByExample(RolePermissionEntityExample example);

    RolePermissionEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RolePermissionEntity record, @Param("example") RolePermissionEntityExample example);

    int updateByExample(@Param("record") RolePermissionEntity record, @Param("example") RolePermissionEntityExample example);

    int updateByPrimaryKeySelective(RolePermissionEntity record);

    int updateByPrimaryKey(RolePermissionEntity record);
}
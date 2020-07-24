package com.framework.auth.mapper;

import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.PermissionEntityExample;
import com.framework.dao.base.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PermissionEntityMapper extends BaseMapper {
    int countByExample(PermissionEntityExample example);

    int deleteByExample(PermissionEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PermissionEntity record);

    int insertSelective(PermissionEntity record);

    List<PermissionEntity> selectByExampleWithRowbounds(PermissionEntityExample example, RowBounds rowBounds);

    List<PermissionEntity> selectByExample(PermissionEntityExample example);

    PermissionEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PermissionEntity record, @Param("example") PermissionEntityExample example);

    int updateByExample(@Param("record") PermissionEntity record, @Param("example") PermissionEntityExample example);

    int updateByPrimaryKeySelective(PermissionEntity record);

    int updateByPrimaryKey(PermissionEntity record);
}
package com.framework.auth.mapper;

import com.framework.auth.pojo.entity.PpropertiesExtraEntity;
import com.framework.auth.pojo.entity.PpropertiesExtraEntityExample;
import com.framework.dao.base.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PpropertiesExtraEntityMapper extends BaseMapper {
    int countByExample(PpropertiesExtraEntityExample example);

    int deleteByExample(PpropertiesExtraEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PpropertiesExtraEntity record);

    int insertSelective(PpropertiesExtraEntity record);

    List<PpropertiesExtraEntity> selectByExampleWithRowbounds(PpropertiesExtraEntityExample example, RowBounds rowBounds);

    List<PpropertiesExtraEntity> selectByExample(PpropertiesExtraEntityExample example);

    PpropertiesExtraEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PpropertiesExtraEntity record, @Param("example") PpropertiesExtraEntityExample example);

    int updateByExample(@Param("record") PpropertiesExtraEntity record, @Param("example") PpropertiesExtraEntityExample example);

    int updateByPrimaryKeySelective(PpropertiesExtraEntity record);

    int updateByPrimaryKey(PpropertiesExtraEntity record);
}
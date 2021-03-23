package com.test.mapper;

import com.test.pojo.AbcEntity;
import com.test.pojo.AbcEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AbcEntityMapper {
    int countByExample(AbcEntityExample example);

    int deleteByExample(AbcEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AbcEntity record);

    int insertSelective(AbcEntity record);

    List<AbcEntity> selectByExampleWithRowbounds(AbcEntityExample example, RowBounds rowBounds);

    List<AbcEntity> selectByExample(AbcEntityExample example);

    AbcEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AbcEntity record, @Param("example") AbcEntityExample example);

    int updateByExample(@Param("record") AbcEntity record, @Param("example") AbcEntityExample example);

    int updateByPrimaryKeySelective(AbcEntity record);

    int updateByPrimaryKey(AbcEntity record);
}
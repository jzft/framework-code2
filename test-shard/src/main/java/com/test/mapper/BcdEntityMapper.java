package com.test.mapper;

import com.test.pojo.BcdEntity;
import com.test.pojo.BcdEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BcdEntityMapper {
    int countByExample(BcdEntityExample example);

    int deleteByExample(BcdEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BcdEntity record);

    int insertSelective(BcdEntity record);

    List<BcdEntity> selectByExampleWithRowbounds(BcdEntityExample example, RowBounds rowBounds);

    List<BcdEntity> selectByExample(BcdEntityExample example);

    BcdEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BcdEntity record, @Param("example") BcdEntityExample example);

    int updateByExample(@Param("record") BcdEntity record, @Param("example") BcdEntityExample example);

    int updateByPrimaryKeySelective(BcdEntity record);

    int updateByPrimaryKey(BcdEntity record);
}
package com.framework.dao.base;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;



public interface BaseMapper<T, TExample> {

	Long countByExample(TExample example);

	int deleteByExample(TExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(T record);

	int insertSelective(T record);

	List<T> selectByExampleWithBLOBsWithRowbounds(TExample example, RowBounds rowBounds);

	List<T> selectByExampleWithBLOBs(TExample example);

	List<T> selectByExampleWithRowbounds(TExample example, RowBounds rowBounds);

	List<T> selectByExample(TExample example);


	int updateByExampleSelective(@Param("record") T record, @Param("example") TExample example);

	int updateByExampleWithBLOBs(@Param("record") T record, @Param("example") TExample example);

	int updateByExample(@Param("record") T record, @Param("example") TExample example);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKeyWithBLOBs(T record);
	


    T selectByPrimaryKey(Integer id);

	 /**
     * 这是Mybatis Generator拓展插件生成的方法(请勿删除).
     * This method corresponds to the database table s_task_templete
     *
     * @mbg.generated
     * @author hewei
     */
    int batchInsert(@Param("list") List<T> list);

}
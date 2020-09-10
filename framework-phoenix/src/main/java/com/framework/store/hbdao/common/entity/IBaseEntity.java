package com.framework.store.hbdao.common.entity;



import java.io.Serializable;

/**
 * 实体对象接口
 * @date 2012-9-8
 */
public interface IBaseEntity extends Serializable  {
	/**
	 * 获得标识ID
	* @return Serializable 
	 */
	String getId();
	/**
	 * 设置ID标识
	 * @param id
	 */
	void setId(String id);
}

package com.framework.store.hbdao.common;

import java.io.Serializable;
import java.util.List;

import com.framework.store.hbdao.common.entity.IBaseEntity;


/**
 * 数据访问层接口定义
 * 
 * @date 2012-9-8
 */
public interface IBaseDao {
	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	public <T extends IBaseEntity> T get(Class<T> clazz, String id);

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	// public <T extends IBaseEntity> T find(Class<T> clazz, Serializable id);
	/**
	 * 是否存在ID的实体
	 * 
	 * @return boolean
	 */
	public <T extends IBaseEntity> boolean exists(Class<T> clazz, String id);

	/**
	 * 获取实体列表
	 * 
	 * @return
	 */
	public <T extends IBaseEntity> List<T> findAll(Class<T> clazz);

	/**
	 * 创建实体
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void create(T o);

	/**
	 * 批量创建实体
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void create(List<T> instances);

	/**
	 * 更新实体
	 * 
	 * @param o
	 *            实体对象
	 */
	public <T extends IBaseEntity> void update(T o);

	/**
	 * 批量创建实体
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void update(List<T> instances);

	/**
	 * 根据ID删除实体对象
	 * 
	 * @param id
	 */
	public <T extends IBaseEntity> void delete(Class<T> clazz, String id);

	/**
	 * 删除实体对象
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void delete(T o);

	/**
	 * 批量删除实体
	 * 
	 * @param o
	 *//*
		 * public <T extends IBaseEntity> void delete(List<T> instances);
		 */
	/**
	 * 保存实体对象
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void save(T o);

	/**
	 * 批量保存实体对象
	 * 
	 * @param o
	 */
	public <T extends IBaseEntity> void save(List<T> instances);

	public <T extends IBaseEntity> List<T> query(Class<T> clazz, String sql);

//	public <T extends IBaseEntity> void create_new(T o);
}

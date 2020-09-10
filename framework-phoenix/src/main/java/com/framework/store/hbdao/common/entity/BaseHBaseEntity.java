package com.framework.store.hbdao.common.entity;




/**
 * hbase实体基类
 *
 */
public abstract class BaseHBaseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8227836577259237059L;
	/**
	 * hbase的rowkey生成
	 * @return
	 */
	public abstract String generateRowKey();
	
	/**
	 * 设置phoenix SALT_BUCKETS
	 * @return
	 */
	public abstract String setSaltBuckets();

	/**
	 * 设置 Hbase 过期时间,单位为秒
	 * @return
	 */
	public abstract String setTimeToLive();
	
	/**
	 * 设置Hbase 时间戳
	 * @return
	 */
	public abstract String setTimeStamp(String trueTtl);
	
	
}

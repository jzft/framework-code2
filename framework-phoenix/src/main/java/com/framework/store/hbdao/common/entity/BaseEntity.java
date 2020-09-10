package com.framework.store.hbdao.common.entity;

import com.alibaba.fastjson.JSON;

/**
 * 实体基类
 *
 */
public abstract class BaseEntity implements IBaseEntity {

	private static final long serialVersionUID = -4945062958387483723L;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String convertJson(){
		return JSON.toJSONString(this);
	}

}

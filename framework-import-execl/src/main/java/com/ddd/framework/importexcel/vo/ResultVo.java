package com.ddd.framework.importexcel.vo;

import java.util.List;

public class ResultVo <T> {

	private Integer status = 1;
	private String msg = null;
	
	private List<T> list = null;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}

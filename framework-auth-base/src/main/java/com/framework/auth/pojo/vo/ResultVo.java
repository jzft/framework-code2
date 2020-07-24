package com.framework.auth.pojo.vo;

import io.swagger.annotations.ApiModelProperty;

public class ResultVo<T> extends BaseVo {
	  /**
     * 响应数据
     */
	@ApiModelProperty(name = "响应数据")
	private T data;
	
	public ResultVo(){}
	public ResultVo(int code,String msg) {
		// TODO Auto-generated constructor stub
		super(code,msg);
	}
	
	public ResultVo(int code,String msg, T data) {
		// TODO Auto-generated constructor stub
		super(code,msg);
		this.data  = data;
	}

	
	
	 public static <T> ResultVo<T> build(int code,String msg){
	        return new ResultVo<>(code,msg);
	 }
	 
	 public static <T> ResultVo<T> build(int code,String msg,T data){
	        return new ResultVo<>(code,msg,data);
	 }
	
	 public T getData() {
	        return data;
	    }

	    public void setData(T data) {
	        this.data = data;
	    }
}

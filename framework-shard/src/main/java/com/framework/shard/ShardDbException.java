package com.framework.shard;

/**
 * 切片异常特殊处理
 * @author lyq
 *
 */
public class ShardDbException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShardDbException(String message, Throwable cause){
		super(message, cause);
	}
	public ShardDbException(String message){
		super(message);
	}
}

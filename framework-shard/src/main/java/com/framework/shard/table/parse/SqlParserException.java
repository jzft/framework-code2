package com.framework.shard.table.parse;

public class SqlParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlParserException(String msg){
		super(msg);
		
	}
	public SqlParserException(Throwable throwable){
		super(throwable);
		
	}
}

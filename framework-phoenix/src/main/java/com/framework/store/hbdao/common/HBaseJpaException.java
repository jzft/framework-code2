package com.framework.store.hbdao.common;



public class HBaseJpaException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3415142030721867367L;
	private static final long E_CODE = 3000;

	public HBaseJpaException(){
		super(String.valueOf(E_CODE));
	}
	
	public HBaseJpaException(int code){
		super(String.valueOf(code));
	}
	
	public HBaseJpaException(String message){
		super(message);
	}
	
	public HBaseJpaException(Exception ex){
		super("HBase异常,"+String.valueOf(E_CODE), ex);
	}
	
	public HBaseJpaException(String message,Throwable rootCause){
		super(message,rootCause);
	}
	
	public HBaseJpaException(String message, Exception ex){
		super(message,ex);
	}
}

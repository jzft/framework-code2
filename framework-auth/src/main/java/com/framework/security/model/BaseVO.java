package com.framework.security.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public abstract class BaseVO implements Serializable {

	private static final long serialVersionUID = -4221487021147081721L;
	
    /** 
     *  @Title:        load 
     * @Description:  copy orig中 相同的属性到this 
     * @param:        @param orig    
     * @return:       void    
     * @throws 		  IllegalAccessException,InvocationTargetException
     * @author        join
     * @Date          2017年1月6日 上午8:58:47 
     */
    public void load(Object orig) {
    	try {
			BeanUtils.copyProperties(this, orig);
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    }

}
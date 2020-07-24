package com.framework.cache.lock;

@FunctionalInterface
public interface LockCallback<T> {

	
	public T exec();
}

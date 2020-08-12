package com.framework.shard.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Stack;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.framework.shard.annotation.Shard;

@Aspect
@Component
public class ClassPathMapperScannerAop {
	@Around("execution(* org.mybatis.spring.mapper.ClassPathMapperScanner.doScan(..))")
	public Object doSplitShardAroud(ProceedingJoinPoint  joinPoint) throws Throwable {
			Object[] pkgs = joinPoint.getArgs();
			if(pkgs!=null){
				for(int i=0 ;i<pkgs.length;i++){
					Object pkg = pkgs[i];
					if(pkg instanceof String){
						String pkgStr = StringUtils.trim((String)pkg);
						if(pkgStr.matches("^\\$\\{[\\d\\w]*\\}$")){
							
						}
					}
				}
			}
		   return joinPoint.proceed();
	   
	 }
}

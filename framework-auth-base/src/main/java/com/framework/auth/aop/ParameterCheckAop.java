package com.framework.auth.aop;

import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.vo.BaseVo;
import com.framework.auth.pojo.vo.ResultVo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * date 2020/6/19 上午10:48
 *
 * @author casper
 **/
@Aspect
@Component
public class ParameterCheckAop {

    @Pointcut(value = "execution(* com.auth.api..*(..))")
    public void pointCut(){

    }


    @Around(value = "pointCut() && args(..,errors)")
    public Object process(ProceedingJoinPoint joinPoint, Errors errors) throws Throwable {
        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResultVo.build(ResultCodeEnum.PARAMETER_ERROR.getCode(),allErrors.get(0).getDefaultMessage());
        }
        return joinPoint.proceed();
    }


}
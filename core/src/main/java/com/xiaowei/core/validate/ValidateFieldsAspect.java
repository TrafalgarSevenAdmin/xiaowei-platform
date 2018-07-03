package com.xiaowei.core.validate;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 字段验证切面
 * @Version 1.0
 */
@Aspect
@Component
public  class ValidateFieldsAspect {

    @Pointcut("@annotation(com.xiaowei.core.validate.AutoErrorHandler)")
    public  void aspect() {
    }

    @Around("aspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;
                if(bindingResult.hasErrors()){
                    throw new FieldErrorsException("字段验证错误",bindingResult.getFieldErrors());
                }
            }
        }
        Object proceed = joinPoint.proceed();
        return proceed;
    }

}

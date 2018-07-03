package com.xiaowei.commonlog4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yuanxuan on 2018/4/16.
 * 该注解形成切面来指定日志信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleLog {

    /**
     * 日志的操作类型
     * @return
     */
    String type();

    /**
     * 需要录入日志的参数名称
     * @return
     */
    ContentParam[] contentParams() default {};

}

package com.xiaowei.commonlog4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yuanxuan on 2018/4/16.
 * 指定对象那些字段录入到日志内容当中
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamField {
    /**
     * 字段录入的名称
     * @return
     */
    String value();
}

package com.xiaowei.commonlog4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yuanxuan on 2018/4/16.
 * 记录到内容里面的参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ContentParam {

    /**
     * 参数名称
     * @return
     */
    String field();

    /**
     * 日志录入的对象名称
     * @return
     */
    String value();

    /**
     * 是否使用ParamField来作为对象录入内容的参照:false表示不使用,就按照json格式录入参数内容
     *                                            true表示使用,就按照对象有 @ParamField 的字段来录入参数内容
     * @return
     */
    boolean useParamField() default false;
}

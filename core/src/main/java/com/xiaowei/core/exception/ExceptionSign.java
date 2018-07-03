package com.xiaowei.core.exception;

import java.lang.annotation.*;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 异常标记
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionSign {

    Class<? extends Throwable>[] value();

    int order() default 0;

}

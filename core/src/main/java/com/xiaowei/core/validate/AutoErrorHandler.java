package com.xiaowei.core.validate;

import java.lang.annotation.*;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 自动处理接口调取错误标记
 * @Version 1.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoErrorHandler {

}

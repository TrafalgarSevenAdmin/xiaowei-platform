package com.xiaowei.core.helper;

import org.springframework.context.ApplicationContext;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description spring context 上下文助手类
 * @Version 1.0
 */
public class SpringContextHelper {

    private static ApplicationContext applicationContext;


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHelper.applicationContext = applicationContext;
    }
}

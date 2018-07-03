package com.xiaowei.core;

import com.xiaowei.core.helper.SpringContextHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhouyang
 * @Date 2018-03-01 11:47
 * @Description
 * @Version 1.0
 */
@Component
public class SpringContext implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHelper.setApplicationContext(applicationContext);
    }
}

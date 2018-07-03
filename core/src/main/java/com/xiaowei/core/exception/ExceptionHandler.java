package com.xiaowei.core.exception;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 异常处理接口
 * @Version 1.0
 */
public interface ExceptionHandler {

    /**
     * 异常处理
     * @param e
     * @param view
     * @return  如果返回true 则处理此异常 返回false 则不处理
     */
    public void handler(Throwable e, ModelAndView view);

}

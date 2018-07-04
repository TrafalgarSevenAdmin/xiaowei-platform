package com.xiaowei.core.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.context.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 异常处理拦截器
 * @Version 1.0
 */
@Component
public class ExceptionHandlerInterceptor implements HandlerExceptionResolver,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerInterceptor.class);

    private static Map<String,List<ExceptionHandler>> exceptionHandlerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionHandlerMap = new HashMap<String,List<ExceptionHandler>>();
        String[] exceptionHandler = ContextUtils.getApplicationContext().getBeanNamesForType(ExceptionHandler.class);
        for (String exception : exceptionHandler) {
            //获取所有的异常处理区
            ExceptionHandler handler = ContextUtils.getApplicationContext().getBean(exception, ExceptionHandler.class);
            ExceptionSign exceptionSign = handler.getClass().getDeclaredAnnotation(ExceptionSign.class);
            if(exceptionSign == null){
                throw new RuntimeException(handler.getClass().getName() + "这个类未标记需要处理什么异常!");
            }
            //按异常处理进行分组
            for (Class<? extends Throwable> aClass : exceptionSign.value()) {
                List<ExceptionHandler> exceptionHandlers = exceptionHandlerMap.get(aClass.getName());
                if(exceptionHandlers == null){
                    exceptionHandlers = new ArrayList<>();
                    exceptionHandlerMap.put(aClass.getName(),exceptionHandlers);
                }
                exceptionHandlers.add(handler);
            }
        }

        //对异常处理进行优先级排序
        exceptionHandlerMap.forEach((String s, List<ExceptionHandler> exceptionHandlers) -> {
            if(exceptionHandlers.size() > 1){
                //检查如果处理器无法区分谁是最大优先级的则报错
                int count = 0;
                int order = 0;
                List<String> exhNames = new ArrayList<>();
                for (int i = 0; i < exceptionHandlers.size(); i++) {
                    ExceptionHandler exh = exceptionHandlers.get(i);
                    ExceptionSign exceptionSign = exh.getClass().getDeclaredAnnotation(ExceptionSign.class);
                    if(i == 0){
                        order = exceptionSign.order();
                    }else{
                        if(order == exceptionSign.order()){
                            count ++;
                        }
                    }
                    exhNames.add(exh.getClass().getName());
                }

                if(count == exceptionHandlers.size()){
                    throw new RuntimeException(exhNames.toString() + "这几个异常处理器优先级一样 无法区分请设置优先级!");
                }

            }
            Collections.sort(exceptionHandlers, new Comparator<ExceptionHandler>() {
                @Override
                public int compare(ExceptionHandler o1, ExceptionHandler o2) {
                    ExceptionSign exceptionSign1 = o1.getClass().getDeclaredAnnotation(ExceptionSign.class);
                    ExceptionSign exceptionSign2 = o2.getClass().getDeclaredAnnotation(ExceptionSign.class);
                    return new Integer(exceptionSign1.order()).compareTo(new Integer(exceptionSign2.order()));
                }
            });
        });
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        FastJsonJsonView view = new FastJsonJsonView();
        ModelAndView mv = new ModelAndView();

        //打印错误信息
        logger.error("异常处理器",ex);


        //寻找异常处理器处理
        try{
            List<ExceptionHandler> exceptionHandlers = exceptionHandlerMap.get(ex.getClass().getName());
            if(!CollectionUtils.isEmpty(exceptionHandlers)){
                ExceptionHandler exceptionHandler = exceptionHandlers.get(exceptionHandlers.size() - 1);
                exceptionHandler.handler(ex,mv);
                return mv;
            }
        }catch (Exception e){
        }

        //如果未找到匹配的 使用默认的异常处理器
        List<ExceptionHandler> exceptionHandlerList = exceptionHandlerMap.get(Exception.class.getName());
        exceptionHandlerList.get(exceptionHandlerList.size() - 1).handler(ex,mv);
        return mv;
    }


}

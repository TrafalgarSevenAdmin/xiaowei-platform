package com.xiaowei.core.context;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhouyang
 * @Date 2017-04-13 下午4:56
 * @Description 上下文初始化拦截器
 * @Version 1.0
 */
@WebFilter(urlPatterns = "/*")
@Order(1)
@Component
public class ContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        System.out.println("sessionId:" + req.getSession().getId());
        ContextData contextData = new ContextData();
        contextData.setRequest(req);
        contextData.setResponse(rep);
        ContextUtils.setContexeData(contextData);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}

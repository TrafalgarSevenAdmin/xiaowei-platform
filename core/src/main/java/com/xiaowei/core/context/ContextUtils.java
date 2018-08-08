package com.xiaowei.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 上下文数据工具
 * @Version 1.0
 */
@Component
public class ContextUtils implements ApplicationContextAware {
    private static ThreadLocal<ContextData> contextDataThreadLocal = new ThreadLocal<ContextData>();

    private static ApplicationContext applicationContext;

    public static void setContexeData(ContextData contexeData){
        contextDataThreadLocal.set(contexeData);
    }

    public static ContextData getContexeData(){
        ContextData contextData = contextDataThreadLocal.get();
        if(contextData == null){
            contextDataThreadLocal.set(new ContextData());
        }
        return contextDataThreadLocal.get();
    }

    public static HttpServletRequest getRequest(){
        ContextData contextData = contextDataThreadLocal.get();
        return contextData != null ? contextData.getRequest() : null;
    }

    public static HttpServletResponse getResponse(){
        ContextData contextData = contextDataThreadLocal.get();
        return contextData != null ? contextData.getResponse() : null;
    }



    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * @Description: 获取客户端IP地址
     */
    public static String getIpAddr() {
        HttpServletRequest request = ContextUtils.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

    public static String judgeAddress(String remoteAddr){
        if("0:0:0:0:0:0:0:1".equals(remoteAddr)){
            remoteAddr = "127.0.0.1";
        }
        return remoteAddr;
    }


}

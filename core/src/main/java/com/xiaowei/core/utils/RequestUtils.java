package com.xiaowei.core.utils;

import com.xiaowei.core.context.ContextUtils;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @return
     */
    public static String getOsAndBrowserInfo(){
        return getOsAndBrowserInfo(ContextUtils.getRequest());
    }

    public static String getOsAndBrowserInfo(HttpServletRequest request){
        String  browserDetails  =   request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(browserDetails);
        return userAgent.getOperatingSystem() + "---" + userAgent.getBrowser();
    }
}

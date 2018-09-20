package com.xiaowei.commonlog4j.aspect;

import com.alibaba.fastjson.JSONObject;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.commonlog4j.entity.LogBaseData;
import com.xiaowei.commonlog4j.service.ILogBaseDataService;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanxuan on 2018/4/16.
 * 操作日志切面
 */
@Aspect
@Component
@Order(1)
public class HandleLogAspect {

    @Autowired
    private ILogBaseDataService logBaseDataService;

//    private Logger logger = LoggerFactory.getLogger(HandleLog.class);

    /**
     * Description: 定义切点名controllerAspect
     */
    @Pointcut("@annotation(com.xiaowei.commonlog4j.annotation.HandleLog)")
    public void controllerAspect() {
    }

    /**
     * Description:环绕增强
     */
    @Around("controllerAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取返回值
        Object res = null;
        try {
            res = joinPoint.proceed();
        } catch (Exception e) {
            //记录失败日志
            saveFailureLog(joinPoint, e.getMessage());
            throw e;
        }

        if (res == null) {
            return null;//不记录
        }
        if (res instanceof Result) {
            //判断是否执行成功
            Result result = (Result) res;
            if (!result.isSuccess()) {
                //记录失败日志
                saveFailureLog(joinPoint, result.getMsg());
            } else {
                //记录成功日志
                saveSuccessLog(joinPoint);
            }
        } else {
            //记录成功日志
            saveSuccessLog(joinPoint);
        }
        return res;
    }

    /**
     * 记录成功日志
     *
     * @param joinPoint
     *
     */
    private void saveSuccessLog(ProceedingJoinPoint joinPoint) throws IllegalAccessException {
        //类名
        String targetName = joinPoint.getTarget().getClass().getSimpleName();
        //得到方法名
        String methodName = joinPoint.getSignature().getName();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //入参key
        String[] parameterNames = ms.getParameterNames();
        //入参value
        Object[] arguments = joinPoint.getArgs();
        Method method = ms.getMethod();
        //方法的注解对象
        HandleLog logParam = method.getAnnotation(HandleLog.class);

        //创建成功日志对象
        LogBaseData logBaseData = new LogBaseData();
        logBaseData.setType(logParam.type());
        logBaseData.setHandleTime(new Date());
        logBaseData.setSuccess(true);
        logBaseData.setUrl(targetName + "." + methodName);
        logBaseData.setIp(ContextUtils.judgeAddress(ContextUtils.getIpAddr()));
        logBaseData.setEmployeeId(LoginUserUtils.getLoginUser().getId());

        //设置日志内容
        setLogContent(logBaseData, logParam.contentParams(), parameterNames, arguments);
        logBaseDataService.save(logBaseData);
    }

    /**
     * 记录失败日志
     *
     * @param joinPoint
     */
    private void saveFailureLog(ProceedingJoinPoint joinPoint, String msg) throws IllegalAccessException {
        //类名
        String targetName = joinPoint.getTarget().getClass().getSimpleName();
        //得到方法名
        String methodName = joinPoint.getSignature().getName();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //入参key
        String[] parameterNames = ms.getParameterNames();
        //入参value
        Object[] arguments = joinPoint.getArgs();
        Method method = ms.getMethod();
        //方法的注解对象
        HandleLog logParam = method.getAnnotation(HandleLog.class);

        //创建失败日志对象
        LogBaseData logBaseData = new LogBaseData();
        logBaseData.setCause(msg);
        logBaseData.setType(logParam.type());
        logBaseData.setHandleTime(new Date());
        logBaseData.setSuccess(false);
        logBaseData.setUrl(targetName + "." + methodName);
        logBaseData.setIp(ContextUtils.judgeAddress(ContextUtils.getIpAddr()));
        logBaseData.setEmployeeId(LoginUserUtils.getLoginUser().getId());

        //设置日志内容
        setLogContent(logBaseData, logParam.contentParams(), parameterNames, arguments);
        logBaseDataService.save(logBaseData);
    }

    /**
     * 设置日志内容
     * @param logBaseData
     * @param contentParams
     * @param parameterNames
     * @param arguments
     * @throws IllegalAccessException
     */
    private void setLogContent(LogBaseData logBaseData, ContentParam[] contentParams, String[] parameterNames, Object[] arguments) throws IllegalAccessException {
        if (contentParams == null || contentParams.length == 0) {
            return;
        }
        //切面方法的参数的键值对集合
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], arguments[i]);
        }

        StringBuffer jsonParamSb = new StringBuffer();
        //遍历需要录入的参数组合成内容
        for (int i = 0; i < contentParams.length; i++) {
            ContentParam contentParam = contentParams[i];
            //参数值
            Object paramValue = paramMap.get(contentParam.field());
            if (paramValue != null) {
                //日志录入的对象名称
                String value = contentParam.value();
                //是否使用ParamField来作为对象录入内容的参照
                if (contentParam.useParamField()) {
                    jsonParamSb.append(value).append("=").append("{" + getParamFieldString(paramValue) + "}").append("&");
                } else {
                    jsonParamSb.append(value).append("=").append(JSONObject.toJSONString(paramValue)).append("&");
                }
            }
        }
        if (jsonParamSb.length() != 0) {
            //去掉末尾的&
            jsonParamSb.deleteCharAt(jsonParamSb.length() - 1);
        }
        logBaseData.setContent(jsonParamSb.toString());
    }

    private String getParamFieldString(Object paramValue) throws IllegalAccessException {
        String paramFieldString = "";
        Field[] fields = paramValue.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ParamField annotation = field.getAnnotation(ParamField.class);
            if (annotation != null) {
                Object obj = field.get(paramValue);
                if (obj == null) {
                    paramFieldString = paramFieldString + annotation.value() + ":" + "**" + ",";
                } else {
                    paramFieldString = paramFieldString + annotation.value() + ":" + JSONObject.toJSONString(obj) + ",";
                }
            }
        }
        if (StringUtils.isNotEmpty(paramFieldString)) {
            //去掉最后的逗号
            paramFieldString = paramFieldString.substring(0, paramFieldString.length() - 1);
        }
        return paramFieldString;
    }

}

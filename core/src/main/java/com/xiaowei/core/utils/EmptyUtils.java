package com.xiaowei.core.utils;

import com.xiaowei.core.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class EmptyUtils {

    /**
     * 对象如果为空,则抛出异常
     * @param obj
     * @param msg
     * @throws Throwable
     */
    public static void assertObject(Object obj, String msg){
        if (obj == null) {
            throw new BusinessException(msg);
        }
    }

    /**
     * 对象如果不为空,则抛出异常
     * @param obj
     * @param msg
     * @throws Throwable
     */
    public static void assertObjectNotNull(Object obj, String msg){
        if (obj != null) {
            throw new BusinessException(msg);
        }
    }

    /**
     * 字符串如果为空,则抛出异常
     * @param s
     * @param msg
     * @throws Throwable
     */
    public static void assertString(String s, String msg){
        if (StringUtils.isEmpty(s)) {
            throw new BusinessException(msg);
        }
    }

    /**
     * optional如果没有值,则抛出异常
     * @param optional
     * @param msg
     * @throws Throwable
     */
    public static void assertOptional(Optional optional, String msg){
        try {
            optional.orElseThrow(() -> new BusinessException(msg));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BusinessException(throwable.getMessage());
        }
    }

    /**
     * optional如果有值,则抛出异常
     * @param optional
     * @param msg
     */
    public static void assertOptionalNot(Optional optional, String msg) {
        optional.ifPresent(o -> {
            throw new BusinessException(msg);
        });
    }
}

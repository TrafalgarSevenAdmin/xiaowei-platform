package com.xiaowei.account.utils;

import com.xiaowei.account.entity.SysConfig;
import com.xiaowei.account.service.ISysConfigService;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.exception.BusinessException;

/**
 * 配置管理
 * 注意，最好不要在程序启动期间调用此代码，否者可能也许会因为全局上下文未注入造成空指针错误
 */
public class ConfigUtils {

    /**
     * 获取配置，配置是必须的，如果找不到配置，就会报错
     * @param code
     * @return
     */
    public static String getConfigValue(String code) {
        SysConfig config = getConfig(code);
        if (config == null) {
            throw new BusinessException("未找到系统配置的" + code + "!请联系管理员配置");
        }
        return config.getValue();
    }

    /**
     * 获取配置，配置是必须的，如果找不到配置，就会取默认配置
     * @param code
     * @return
     */
    public static String getConfigValueOrDefault(String code,String defaultStr) {
        SysConfig config = getConfig(code);
        if (config == null) {
            return defaultStr;
        }
        return config.getValue();
    }

    /**
     * 获取配置，配置若找不到就会返回null
     * @param code
     * @return
     */
    public static SysConfig getConfig(String code) {
        ISysConfigService sysConfigService = ContextUtils.getApplicationContext().getBean(ISysConfigService.class);
        return sysConfigService.findByCode(code);
    }
}

package com.xiaowei.commondict.region;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhouyang
 * @Date 2017-09-11 21:48
 * @Description
 * @Version 1.0
 */
public class RegionUtils {

    /**
     * 补全14位行政区代码
     * @param code
     * @return
     */
    public static String completed(String code){
        if(!StringUtils.isEmpty(code)){
            int len = RegionLevelEnum.GROUP.getLength() - code.length();
            StringBuilder buf = new StringBuilder();
            buf.append(code);
            for (int i = 1; i <= len; i++) {
                buf.append("0");
            }
            return buf.toString();
        }
        return null;
    }

    /**
     * 根据行政区等级获取行政区编码
     * @param regionLevelEnum
     * @param code
     * @return
     */
    public static String getCode(RegionLevelEnum regionLevelEnum,String code){
        if(!StringUtils.isEmpty(code)){
            code = completed(code);
            return code.substring(0,regionLevelEnum.getLength());
        }
        return null;
    }
}

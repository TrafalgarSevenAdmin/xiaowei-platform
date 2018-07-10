package com.xiaowei.wechat.consts;

/**
 * 存储魔法值的仓库
 */
public class MagicValueStore {
    /**
     * 微信state统计拉取授权次数在redis中的key前缀
     * 为了更好的分组查看
     */
    public static final String wxStatesNumberPro = "wechat:states:number";

    /**
     * 微信state字段在redis中的key前缀
     * 为了更好的分组查看
     */
    public static final String wxStatesValuePro = "wechat:states:value";
}

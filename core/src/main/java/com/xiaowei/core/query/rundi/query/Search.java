package com.xiaowei.core.query.rundi.query;

/**
 * @author zhouyang
 * @Date 2017-05-15 11:51
 * @Description 全文检索对象
 * @Version 1.0
 */
public class Search {

    /**
     * 根据条件匹配的字段
     */
    private String fields;

    /**
     * 根据匹配的value
     */
    private String value;

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package com.xiaowei.core.result;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yuanxuan on 2018/4/10.
 * 该对象的作用为指定返回对象的包含字段
 */
public class FieldsView {

    private Set<String> fields = new HashSet<>();

    /**
     * true 包含 false 不包含
     */
    private boolean include = false;

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }
}

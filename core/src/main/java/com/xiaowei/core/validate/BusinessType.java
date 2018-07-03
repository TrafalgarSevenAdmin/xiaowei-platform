package com.xiaowei.core.validate;

public enum BusinessType {

    INSERT("insert"),

    UPDATE("update"),

    DELETE("delete");

    private String code;

    BusinessType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

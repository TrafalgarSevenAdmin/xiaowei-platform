package com.xiaowei.wechat.dto;

public enum PayType {
    JSAPI("JSAPI"),

    QR("");


    String type;
    PayType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

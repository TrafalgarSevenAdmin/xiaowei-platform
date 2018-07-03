package com.xiaowei.core.exception;

/**
 * @author zhouyang
 * @Date 2017-09-14 11:12
 * @Description
 * @Version 1.0
 */
public class FieldErrorModel {

    private String message;

    private String type;

    private String field;

    public FieldErrorModel() {

    }

    public FieldErrorModel(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public FieldErrorModel(String message, String type, String field) {
        this.message = message;
        this.type = type;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

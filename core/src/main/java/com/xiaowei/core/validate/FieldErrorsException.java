package com.xiaowei.core.validate;

import org.springframework.validation.FieldError;

import java.util.List;


/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 字段错误异常
 * @Version 1.0
 */
public class FieldErrorsException extends RuntimeException{

    private  List<FieldError> fieldErrors;

    public FieldErrorsException() {
    }

    public FieldErrorsException(String message) {
        super(message);
    }

    public FieldErrorsException(String message, List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

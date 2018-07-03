package com.xiaowei.core.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 自定义数据返回格式封装
 * @Version 1.0
 */
public class Result implements Serializable {

    private Integer code;

    private String  msg;

    private Object data;

    private Object errors;

    private boolean success;

    public Result() {

    }

    private Result(Integer code, String msg, Object data, Object errors,boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.errors = errors;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static  Result getSuccess(){
        return new Result(Code.SUCCESS.getCode(),null,null,null,true);
    }

    public static  Result getSuccess(Object data){
        return new Result(Code.SUCCESS.getCode(),null,data,null,true);
    }

    public static Result getError(String msg){
        return new Result(Code.SUCCESS.getCode(),msg,null,null,false);
    }

    public static Result getError(List errors){
        return new Result(Code.SUCCESS.getCode(),null,null,errors,false);
    }

    public static Result getError(Integer code,String msg){
        return new Result(code,msg,null,null,false);
    }

    public static Result getError(Integer code,String msg,Object errors){
        return new Result(code,msg,null,errors,false);
    }

    public static Result getInstance(Integer code, String msg, Object data, Object errors,boolean success){
        return new Result(code,msg,data,errors,success);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

}

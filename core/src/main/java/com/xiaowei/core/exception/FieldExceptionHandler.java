package com.xiaowei.core.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.validate.FieldErrorsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 字段异常处理
 * @Version 1.0
 */
@ExceptionSign(value = FieldErrorsException.class,order = 10)
@Component
public class FieldExceptionHandler implements ExceptionHandler {
    public void handler(Throwable e, ModelAndView view) {
        FastJsonJsonView fv = new FastJsonJsonView();
        FieldErrorsException fieldErrorsException = (FieldErrorsException) e;

        List fieldErrors = new ArrayList<>();
        for (FieldError fieldError : fieldErrorsException.getFieldErrors()) {
            FieldErrorModel fieldErrorModel = new FieldErrorModel();
            fieldErrorModel.setField(fieldError.getField());
            fieldErrorModel.setMessage(fieldError.getDefaultMessage());
            fieldErrorModel.setType(fieldError.getCode());
            fieldErrors.add(fieldErrorModel);
        }

        Result result = Result.getError(Code.BAD_REQUEST.getCode(), e.getMessage(),fieldErrors);
        JSONObject object = (JSONObject) JSON.toJSON(result);
        fv.setAttributesMap(object);
        view.setView(fv);
    }
}

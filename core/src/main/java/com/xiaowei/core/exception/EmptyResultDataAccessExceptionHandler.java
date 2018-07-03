package com.xiaowei.core.exception;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author mocker
 * @Date 2018-03-30 16:11:22
 * @Description
 * @Version 1.0
 */
@ExceptionSign(value = EmptyResultDataAccessException.class,order = 10)
@Component
public class EmptyResultDataAccessExceptionHandler implements ExceptionHandler{

    @Override
    public void handler(Throwable e, ModelAndView view) {
        FastJsonJsonView fv = new FastJsonJsonView();
        if(e.getMessage().contains("No class") && e.getMessage().contains("exists")){
            Result result = Result.getError(Code.SUCCESS.getCode(), "你要删除的数据不存在!");
            JSONObject object = (JSONObject) JSON.toJSON(result);
            fv.setAttributesMap(object);
            view.setView(fv);
        }
    }
}

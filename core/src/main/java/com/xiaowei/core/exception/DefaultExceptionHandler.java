package com.xiaowei.core.exception;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author mocker
 * @Date 2018-03-30 16:11:22
 * @Description 默认Exception处理器
 * @Version 1.0
 */
@ExceptionSign(value = Exception.class,order = 10)
@Component
public class DefaultExceptionHandler implements ExceptionHandler{

    @Override
    public void handler(Throwable e, ModelAndView view) {
        FastJsonJsonView fv = new FastJsonJsonView();
        Result result = Result.getError(Code.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        JSONObject object = (JSONObject) JSON.toJSON(result);
        fv.setAttributesMap(object);
        view.setView(fv);
    }
}

package com.xiaowei.account.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.exception.ExceptionHandler;
import com.xiaowei.core.exception.ExceptionSign;
import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户未登录异常处理
 */
@ExceptionSign(UnauthenticatedException.class)
@Component
public class UnauthenticatedExceptionHandler implements ExceptionHandler {
    public void handler(Throwable e, ModelAndView view) {
        FastJsonJsonView fv = new FastJsonJsonView();
        Result result = Result.getError(Code.UNAUTHORIZED.getCode(),Code.UNAUTHORIZED.getMsg());
        JSONObject object = (JSONObject) JSON.toJSON(result);
        fv.setAttributesMap(object);
        view.setView(fv);
    }
}

package com.xiaowei.account.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.xiaowei.core.exception.ExceptionHandler;
import com.xiaowei.core.exception.ExceptionSign;
import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * 没有权限或权限不足处理
 */
@ExceptionSign(UnauthorizedException.class)
@Component
public class UnauthorizedExceptionHandler implements ExceptionHandler {
    public void handler(Throwable e, ModelAndView view) {
        FastJsonJsonView fv = new FastJsonJsonView();
        Result result = Result.getError(Code.FORBIDDEN.getCode(),e.getMessage());
        if(StringUtils.isEmpty(e.getMessage()) || e.getMessage().contains("Subject does not have permission")){
            result.setMsg(Code.FORBIDDEN.getMsg());
        }
        JSONObject object = (JSONObject) JSON.toJSON(result);
        fv.setAttributesMap(object);
        view.setView(fv);
    }
}

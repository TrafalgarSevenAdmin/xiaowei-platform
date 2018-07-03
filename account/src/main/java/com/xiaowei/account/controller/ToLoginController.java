package com.xiaowei.account.controller;

import com.xiaowei.core.result.Code;
import com.xiaowei.core.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "登录跳转")
@RestController
public class ToLoginController {


    @ApiOperation("处理跳转登录逻辑")
    @GetMapping("/toLogin")
    public Result logout(){
        return Result.getError(Code.UNAUTHORIZED.getCode(),Code.UNAUTHORIZED.getMsg());
    }

}

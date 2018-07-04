package com.xiaowei.worksystem.controller;


import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.worksystem.dto.LoginSysUserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 先使用用户名以及密码登陆，
 * 后期接入公众号后使用微信的oauth授权登陆
 */
@Api(tags = "登录接口")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 用户登录
     * @param loginSysUserDTO
     * @return
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    @AutoErrorHandler
    public Result login(@RequestBody @Validated LoginSysUserDTO loginSysUserDTO, BindingResult bindingResult){
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(loginSysUserDTO.getLoginName(),loginSysUserDTO.getPassword()));
        SysUser sysUser = sysUserService.findByLoginName(loginSysUserDTO.getLoginName());
        AccountUtils.loadUser();
        return Result.getSuccess(LoginUserUtils.getLoginUser());
    }


    /**
     * 退出登录
     * @param
     * @return
     */
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.getSuccess();
    }

}

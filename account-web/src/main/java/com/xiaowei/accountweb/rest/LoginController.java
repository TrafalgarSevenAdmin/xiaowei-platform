package com.xiaowei.accountweb.rest;


import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.LoginSysUserDTO;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.validate.AutoErrorHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

package com.xiaowei.accountweb.rest;


import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountcommon.PermissionBean;
import com.xiaowei.accountweb.dto.LoginSysUserDTO;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
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

import java.util.List;
import java.util.stream.Collectors;

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
    @HandleLog(type = "登录", contentParams = {@ContentParam(useParamField = true, field = "loginSysUserDTO", value = "登录信息")})
    public Result login(@RequestBody @Validated LoginSysUserDTO loginSysUserDTO, BindingResult bindingResult){
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(loginSysUserDTO.getLoginName(),loginSysUserDTO.getPassword()));
//        SysUser sysUser = sysUserService.findByLoginName(loginSysUserDTO.getLoginName());
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
    @HandleLog(type = "退出登录")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.getSuccess();
    }
    /**
     * 获取当前登录用户的信息
     * @param
     * @return
     */
    @ApiOperation("获取当前登录用户的信息")
    @GetMapping("/userInfo")
    public Result userInfo(){
        return Result.getSuccess(LoginUserUtils.getLoginUser());
    }

    /**
     * 获取当前登录用户的信息
     * @param
     * @return
     */
    @ApiOperation("获取当前登录用户的信息")
    @GetMapping("/perCodes")
    public Result perCodes(){
        List<PermissionBean> permissions = LoginUserUtils.getLoginUser().getPermissions();
        return Result.getSuccess(permissions.stream().map(PermissionBean::getSymbol).collect(Collectors.toSet()));
    }

}

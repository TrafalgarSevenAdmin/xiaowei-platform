package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.query.UserQuery;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.accountcommon.CompanyBean;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.SysUserDTO;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 用户接口
 * @Version 1.0
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @RequiresPermissions("account:user:add")
    @ApiOperation(value = "添加用户")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user = sysUserService.saveUser(user);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:update")
    @ApiOperation(value = "修改用户", notes = "只能修改loginName,roles,mobile和email")
    @AutoErrorHandler
    @PutMapping("/{userId}")
    public Result update(@PathVariable("userId") String userId, @RequestBody @Validated(V.Update.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user.setId(userId);
        user = sysUserService.updateUser(user);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:status")
    @ApiOperation(value = "启用/禁用")
    @AutoErrorHandler
    @PutMapping("/{userId}/status")
    public Result updateStatus(@PathVariable("userId") String userId, @RequestBody @Validated(SysUserDTO.UpdateStatus.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user.setId(userId);
        user = sysUserService.updateStatus(user);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:delete")
    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    public Result delete(@PathVariable("userId") String userId) {
        //伪删除
        sysUserService.fakeDeleteUser(userId);
        return Result.getSuccess("删除成功");
    }

    @RequiresPermissions("account:user:get")
    @ApiOperation("根据id获取用户")
    @GetMapping("/{userId}")
//    @HandleLog(type = "根据id获取用户",contentParams = {@ContentParam(field = "userId",value = "用户id")})
    public Result findById(@PathVariable("userId") String userId, FieldsView fieldsView) {
        SysUser user = sysUserService.findById(userId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:query")
    @ApiOperation("用户查询接口")
    @GetMapping("")
    public Result query(UserQuery userQuery, FieldsView fieldsView) {
        //设置默认条件
        setDefaultCondition(userQuery);
        if (userQuery.isNoPage()) {
            List<SysUser> users = sysUserService.query(userQuery,SysUser.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(users, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = sysUserService.queryPage(userQuery,SysUser.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(UserQuery userQuery) {
        //默认只能查询当前登录用户所在公司下的用户
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if(!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())){
            userQuery.getCompanyIds().addAll(loginUser.getCompanyBeans().stream().map(CompanyBean::getId).collect(Collectors.toSet()));
        }
    }


}

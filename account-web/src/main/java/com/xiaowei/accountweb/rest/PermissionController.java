package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.account.query.PermissionQuery;
import com.xiaowei.account.service.ISysPermissionService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountcommon.RoleBean;
import com.xiaowei.accountweb.dto.SysPermissionDTO;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.tree.JsonTreeCreater;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 权限接口
 * @Version 1.0
 */
@Api(tags = "权限接口")
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private ISysPermissionService sysPermissionService;

    @RequiresPermissions("account:permission:add")
    @ApiOperation(value = "添加权限")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加权限", contentParams = {@ContentParam(useParamField = true, field = "sysPermissionDTO", value = "权限信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) SysPermissionDTO sysPermissionDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysPermission permission = BeanCopyUtils.copy(sysPermissionDTO, SysPermission.class);
        permission = sysPermissionService.savePermission(permission);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(permission, fieldsView));
    }

    @RequiresPermissions("account:permission:update")
    @ApiOperation(value = "修改权限")
    @AutoErrorHandler
    @PutMapping("/{permissionId}")
    @HandleLog(type = "修改权限", contentParams = {@ContentParam(useParamField = true, field = "sysPermissionDTO", value = "权限信息"),
            @ContentParam(useParamField = false, field = "permissionId", value = "权限id")})
    public Result update(@PathVariable("permissionId") String permissionId, @RequestBody @Validated(V.Update.class) SysPermissionDTO sysPermissionDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysPermission permission = BeanCopyUtils.copy(sysPermissionDTO, SysPermission.class);
        permission.setId(permissionId);
        permission = sysPermissionService.updatePermission(permission);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(permission, fieldsView));
    }

    @RequiresPermissions("account:permission:delete")
    @ApiOperation("删除权限")
    @DeleteMapping("/{permissionId}")
    @HandleLog(type = "删除权限", contentParams = {@ContentParam(useParamField = false, field = "permissionId", value = "权限id")})
    public Result delete(@PathVariable("permissionId") String permissionId, FieldsView fieldsView) {
        sysPermissionService.deletePermission(permissionId);
        AccountUtils.loadUser();
        return Result.getSuccess("删除成功");
    }

    @RequiresPermissions("account:permission:tree")
    @ApiOperation("权限树查询接口,roleId可传可不传,传的话代表修改角色需要的权限树有checked")
    @GetMapping("/tree")
    public Result tree(String roleId,String prefix) {
        List<SysPermission> permissions;
        if (StringUtils.isNotBlank(prefix)) {
            permissions = sysPermissionService.findBySymbolPrefix(prefix);
        } else {
            permissions = sysPermissionService.findAll();
        }
        Set<String> checkedIds;       //用于显示权限是否被勾选
        if (!StringUtils.isEmpty(roleId)) {
            checkedIds = sysPermissionService.findByRoleId(roleId).stream().collect(Collectors.toSet());
        } else {
            checkedIds = new HashSet<>();
        }
        return Result.getSuccess(new JsonTreeCreater<SysPermission>(permissions,
                item -> item.getCode(),
                a -> StringUtils.isEmpty(a.getParentCode()) ? "0" : a.getParentCode(),
                a -> a.getName(),
                a -> {
                    if (StringUtils.isEmpty(roleId)) {
                        return false;
                    } else {
                        return checkedIds.contains(a.getId());
                    }
                },
                a -> {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("id", a.getId());
                    dataMap.put("symbol", a.getSymbol());
                    dataMap.put("level", a.getLevel());
                    return dataMap;
                },
                a -> !LoginUserUtils.hasPermissionId(a.getId())
        ).create());//以树形式返回
    }

    @RequiresPermissions("account:permission:query")
    @ApiOperation("权限查询接口")
    @GetMapping("")
    public Result query(PermissionQuery permissionQuery, FieldsView fieldsView) {
        //权限查询接口设置默认条件
        setDefaultCondition(permissionQuery);
        if (permissionQuery.isNoPage()) {
            List<SysPermission> permissions = sysPermissionService.query(permissionQuery, SysPermission.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(permissions, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = sysPermissionService.queryPage(permissionQuery, SysPermission.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(PermissionQuery permissionQuery) {
        //默认只能查询当前登录用户所拥有的权限
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if (!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())) {
            permissionQuery.getRoleIds().addAll(loginUser.getRoles().stream().map(RoleBean::getId).collect(Collectors.toSet()));
        }
    }

    @RequiresPermissions("account:permission:get")
    @ApiOperation("根据id获取权限")
    @GetMapping("/{permissionId}")
    public Result findById(@PathVariable("permissionId") String permissionId, FieldsView fieldsView) {
        //根据id获取权限只能获取当前登录用户所拥有的权限
        if (!LoginUserUtils.hasPermissionId(permissionId)) {
            throw new UnauthorizedException("查询失败:没有权限查询该权限");
        }
        SysPermission permission = sysPermissionService.findById(permissionId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(permission, fieldsView));
    }

}

package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.query.RoleQuery;
import com.xiaowei.account.service.ICompanyService;
import com.xiaowei.account.service.ISysPermissionService;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.SysRoleDTO;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 角色接口
 * @Version 1.0
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private ICompanyService companyService;

    @RequiresPermissions("account:role:add")
    @ApiOperation(value = "添加角色", notes = "添加字段name,comment,company")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加角色", contentParams = {@ContentParam(useParamField = true, field = "sysRoleDTO", value = "角色信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) SysRoleDTO sysRoleDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysRole sysRole = BeanCopyUtils.copy(sysRoleDTO, SysRole.class);
        sysRole = sysRoleService.saveRole(sysRole);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysRole, fieldsView));
    }

    /**
     * 只能修改自己所属公司的角色，托管角色不能修改
     */
    @RequiresPermissions("account:role:update")
    @ApiOperation(value = "修改角色", notes = "只能修改name,comment,parentId")
    @AutoErrorHandler
    @PutMapping("/{roleId}")
    @HandleLog(type = "修改岗位", contentParams = {@ContentParam(useParamField = true, field = "sysRoleDTO", value = "角色信息"),
            @ContentParam(useParamField = false, field = "roleId", value = "角色id")})
    public Result update(@PathVariable("roleId") String roleId, @RequestBody @Validated(V.Update.class) SysRoleDTO sysRoleDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysRole sysRole = BeanCopyUtils.copy(sysRoleDTO, SysRole.class);
        sysRole.setId(roleId);
        sysRole = sysRoleService.updateRole(sysRole);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysRole, fieldsView));
    }

    @RequiresPermissions("account:role:delete")
    @ApiOperation("删除角色")
    @DeleteMapping("/{roleId}")
    @HandleLog(type = "删除角色", contentParams = {@ContentParam(field = "roleId", value = "角色id")})
    public Result delete(@PathVariable("roleId") String roleId, FieldsView fieldsView) {
        sysRoleService.deleteRole(roleId);
        AccountUtils.loadUser();
        return Result.getSuccess("删除成功");
    }

    /**
     * 只能查询出自己公司的角色以及托管角色
     * @param roleQuery
     * @param fieldsView
     * @return
     */
    @RequiresPermissions("account:role:query")
    @ApiOperation("角色查询接口")
    @GetMapping("")
    public Result query(RoleQuery roleQuery, FieldsView fieldsView) {
        //强制筛选只能查到刚当前租户下的所有公司的角色。因为角色表无租户概念
        roleQuery.addFilter(new Filter("company.id", Filter.Operator.in, Logic.or,companyService.findAll().stream().map(BaseEntity::getId).toArray()));
        if (roleQuery.isNoPage()) {
            List<SysRole> roles = sysRoleService.query(roleQuery,SysRole.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(roles, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = sysRoleService.queryPage(roleQuery,SysRole.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    @RequiresPermissions("account:role:get")
    @ApiOperation("根据id获取角色")
    @GetMapping("/{roleId}")
    public Result findById(@PathVariable("roleId") String roleId, FieldsView fieldsView) {
        SysRole sysRole = sysRoleService.findById(roleId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysRole, fieldsView));
    }


}

package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.Department;
import com.xiaowei.account.query.DepartmentQuery;
import com.xiaowei.account.service.IDepartmentService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.DepartmentDTO;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
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

/**
 * 部门管理
 */
@Api(tags = "部门接口")
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;


    @RequiresPermissions("account:department:add")
    @ApiOperation(value = "添加部门")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加部门", contentParams = {@ContentParam(useParamField = true, field = "departmentDTO", value = "部门信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) DepartmentDTO departmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Department department = BeanCopyUtils.copy(departmentDTO, Department.class);
        department = departmentService.saveDepartment(department);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(department, fieldsView));
    }

    @RequiresPermissions("account:department:update")
    @ApiOperation(value = "修改部门")
    @AutoErrorHandler
    @PutMapping("/{departmentId}")
    @HandleLog(type = "修改部门", contentParams = {@ContentParam(useParamField = true, field = "departmentDTO", value = "部门信息"),
            @ContentParam(useParamField = false, field = "departmentId", value = "部门id")})
    public Result update(@PathVariable("departmentId") String departmentId, @RequestBody @Validated(V.Update.class) DepartmentDTO departmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Department department = BeanCopyUtils.copy(departmentDTO, Department.class);
        department.setId(departmentId);
        department = departmentService.updateDepartment(department);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(department, fieldsView));
    }

    @RequiresPermissions("account:department:status")
    @ApiOperation(value = "启用/禁用部门")
    @AutoErrorHandler
    @PutMapping("/{departmentId}/status")
    @HandleLog(type = "启用/禁用部门", contentParams = {@ContentParam(field = "departmentId", value = "部门id")})
    public Result updateStatus(@PathVariable("departmentId") String departmentId, @RequestBody @Validated(DepartmentDTO.UpdateStatus.class) DepartmentDTO departmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Department department = BeanCopyUtils.copy(departmentDTO, Department.class);
        department.setId(departmentId);
        department = departmentService.updateStatus(department);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(department, fieldsView));
    }


    @RequiresPermissions("account:department:query")
    @ApiOperation("部门查询接口")
    @GetMapping("")
    public Result query(DepartmentQuery departmentQuery, FieldsView fieldsView) {
        //查询公司设置默认条件
        setDefaultCondition(departmentQuery);
        if (departmentQuery.isNoPage()) {
            List<Department> departments = departmentService.query(departmentQuery, Department.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(departments, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = departmentService.queryPage(departmentQuery, Department.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(DepartmentQuery departmentQuery) {
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if(!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())){
            departmentQuery.setCompanyId(loginUser.getCompanyBean().getId());
        }
    }

    @RequiresPermissions("account:department:get")
    @ApiOperation("根据id获取部门")
    @GetMapping("/{departmentId}")
    public Result findById(@PathVariable("departmentId") String departmentId, FieldsView fieldsView) {
        Department department = departmentService.findById(departmentId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(department, fieldsView));
    }

    @RequiresPermissions("account:department:delete")
    @ApiOperation("删除部门")
    @DeleteMapping("/{departmentId}")
    @HandleLog(type = "删除部门", contentParams = {@ContentParam(field = "departmentId", value = "部门id")})
    public Result delete(@PathVariable("departmentId") String departmentId, FieldsView fieldsView) {
        departmentService.delete(departmentId);
        return Result.getSuccess();
    }

}

package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.Department;
import com.xiaowei.account.query.DepartmentQuery;
import com.xiaowei.account.service.IDepartmentService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.DepartmentDTO;
import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公司管理
 */
@Api(tags = "部门接口")
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private UploadConfigBean uploadConfigBean;

    @Autowired
    private IFileStoreService fileStoreService;


    @RequiresPermissions("account:department:add")
    @ApiOperation(value = "添加部门")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) DepartmentDTO departmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Department department = BeanCopyUtils.copy(departmentDTO, Department.class);
        department = departmentService.saveDepartment(department);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(setLogoPath(department), fieldsView));
    }

    private List<Department> setListLogoPath(List<Department> departments) {
        //部门logoId的集合
        Set<String> collect = departments.stream().filter(department -> StringUtils.isNotEmpty(department.getLogo())).map(Department::getLogo).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(collect)) {
            return departments;
        }
        //设置部门logo视图
        List<FileStore> fileStores = fileStoreService.findByIdIn(collect);
        departments.stream().forEach(department -> {
            String logoId = department.getLogo();
            if (StringUtils.isNotEmpty(logoId)) {
                Optional<FileStore> any = fileStores.stream().filter(fileStore -> logoId.equals(fileStore.getId())).findAny();
                if (any.isPresent()) {
                    department.setLogoPath(uploadConfigBean.getAccessUrlRoot() + any.get().getPath());
                }
            }
        });
        return departments;
    }

    private Department setLogoPath(Department department) {
        //设置部门logo视图
        String logoId = department.getLogo();
        if (StringUtils.isNotEmpty(logoId)) {
            FileStore fileStore = fileStoreService.findById(logoId);
            if (fileStore != null) {
                department.setLogoPath(uploadConfigBean.getAccessUrlRoot() + fileStore.getPath());
            }
        }
        return department;
    }

    @RequiresPermissions("account:department:update")
    @ApiOperation(value = "修改部门")
    @AutoErrorHandler
    @PutMapping("/{departmentId}")
    public Result update(@PathVariable("departmentId") String departmentId, @RequestBody @Validated(V.Update.class) DepartmentDTO departmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Department department = BeanCopyUtils.copy(departmentDTO, Department.class);
        department.setId(departmentId);
        department = departmentService.updateDepartment(department);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(setLogoPath(department), fieldsView));
    }

    @RequiresPermissions("account:department:status")
    @ApiOperation(value = "启用/禁用部门")
    @AutoErrorHandler
    @PutMapping("/{departmentId}/status")
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
            return Result.getSuccess(ObjectToMapUtils.listToMap(setListLogoPath(departments), fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = departmentService.queryPage(departmentQuery, Department.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(setListLogoPath(pageResult.getRows()), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(DepartmentQuery departmentQuery) {
        //默认只能查询自己拥有的部门
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if (!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())) {
            departmentQuery.setUserId(loginUser.getId());
        }
    }

    @RequiresPermissions("account:department:get")
    @ApiOperation("根据id获取部门")
    @GetMapping("/{departmentId}")
    public Result findById(@PathVariable("departmentId") String departmentId, FieldsView fieldsView) {
        //根据id获取角色只能获取当前登录用户所拥有的部门
        if (!LoginUserUtils.hasDepartmentId(departmentId)) {
            throw new UnauthorizedException("查询失败:没有权限查询该部门");
        }
        Department department = departmentService.findById(departmentId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(setLogoPath(department), fieldsView));
    }

}

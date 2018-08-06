package com.xiaowei.accountweb.rest;

import com.xiaowei.account.entity.Company;
import com.xiaowei.account.query.CompanyQuery;
import com.xiaowei.account.service.ICompanyService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.CompanyDTO;
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
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公司管理
 */
@Api(tags = "公司接口")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @RequiresPermissions("account:company:add")
    @ApiOperation(value = "添加公司")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加公司", contentParams = {@ContentParam(useParamField = true, field = "companyDTO", value = "公司信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) CompanyDTO companyDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Company company = BeanCopyUtils.copy(companyDTO, Company.class);
        company = companyService.saveCompany(company);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(company, fieldsView));
    }

    @RequiresPermissions("account:company:update")
    @ApiOperation(value = "修改公司")
    @AutoErrorHandler
    @PutMapping("/{companyId}")
    public Result update(@PathVariable("companyId") String companyId, @RequestBody @Validated(V.Update.class) CompanyDTO companyDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Company company = BeanCopyUtils.copy(companyDTO, Company.class);
        company.setId(companyId);
        company = companyService.updateCompany(company);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(company, fieldsView));
    }

    @RequiresPermissions("account:company:status")
    @ApiOperation(value = "启用/禁用公司")
    @AutoErrorHandler
    @PutMapping("/{companyId}/status")
    public Result updateStatus(@PathVariable("companyId") String companyId, @RequestBody @Validated(CompanyDTO.UpdateStatus.class) CompanyDTO companyDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Company company = BeanCopyUtils.copy(companyDTO, Company.class);
        company.setId(companyId);
        company = companyService.updateStatus(company);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(company, fieldsView));
    }


    @RequiresPermissions("account:company:query")
    @ApiOperation("公司查询接口")
    @GetMapping("")
    public Result query(CompanyQuery companyQuery, FieldsView fieldsView) {
        //查询公司设置默认条件
        setDefaultCondition(companyQuery);

        if (companyQuery.isNoPage()) {
            List<Company> companies = companyService.query(companyQuery, Company.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(companies, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = companyService.queryPage(companyQuery, Company.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(CompanyQuery companyQuery) {

    }

    @RequiresPermissions("account:company:get")
    @ApiOperation("根据id获取公司")
    @GetMapping("/{companyId}")
    public Result findById(@PathVariable("companyId") String companyId, FieldsView fieldsView) {
        //根据id获取角色只能获取当前登录用户所拥有的公司
        if (!LoginUserUtils.hasCompanyId(companyId)) {
            throw new UnauthorizedException("查询失败:没有权限查询该公司");
        }

        Company company = companyService.findById(companyId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(company, fieldsView));
    }


}

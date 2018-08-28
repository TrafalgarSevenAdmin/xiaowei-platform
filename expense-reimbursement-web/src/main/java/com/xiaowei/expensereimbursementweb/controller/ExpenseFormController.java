package com.xiaowei.expensereimbursementweb.controller;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;
import com.xiaowei.expensereimbursement.service.IExpenseFormService;
import com.xiaowei.expensereimbursementweb.dto.ExpenseFormDTO;
import com.xiaowei.expensereimbursementweb.query.ExpenseFormQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报销单管理
 */
@Api(tags = "报销单接口")
@RestController
@RequestMapping("/api/expenseForm")
public class ExpenseFormController {

    @Autowired
    private IExpenseFormService expenseFormService;

    @ApiOperation(value = "添加报销单")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:expenseForm:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) ExpenseFormDTO expenseFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm = expenseFormService.saveExpenseForm(expenseForm);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation(value = "修改报销单")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}")
    @RequiresPermissions("expense:expenseForm:update")
    public Result update(@PathVariable("expenseFormId") String expenseFormId, @RequestBody @Validated(V.Update.class) ExpenseFormDTO expenseFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.updateExpenseForm(expenseForm);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation(value = "报销单初审")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}/first")
    @RequiresPermissions("expense:expenseForm:first")
    public Result firstAudit(@PathVariable("expenseFormId") String expenseFormId,
                             @RequestBody @Validated(ExpenseFormDTO.FirstAudit.class) ExpenseFormDTO expenseFormDTO,
                             BindingResult bindingResult,
                             @RequestParam Boolean audit,
                             FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.firstAudit(expenseForm, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation(value = "报销单复审")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}/second")
    @RequiresPermissions("expense:expenseForm:second")
    public Result secondAudit(@PathVariable("expenseFormId") String expenseFormId,
                              @RequestBody @Validated(ExpenseFormDTO.SecondAudit.class) ExpenseFormDTO expenseFormDTO,
                              BindingResult bindingResult,
                              @RequestParam Boolean audit,
                              FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.secondAudit(expenseForm, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation("当前登录用户查询报销单各种状态数量")
    @GetMapping("/audit/count")
    @RequiresPermissions("expense:expenseForm:auditCount")
    public Result auditCount() {
        final String userId = LoginUserUtils.getLoginUser().getId();
        return Result.getSuccess(expenseFormService.auditCountByUserId(userId));
    }


    @ApiOperation("报销单查询接口")
    @GetMapping("")
    @RequiresPermissions("expense:expenseForm:query")
    public Result query(ExpenseFormQuery expenseFormQuery, FieldsView fieldsView) {
        //查询报销单设置默认条件
        setDefaultCondition(expenseFormQuery);

        if (expenseFormQuery.isNoPage()) {
            List<ExpenseForm> expenseForms = expenseFormService.query(expenseFormQuery, ExpenseForm.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(expenseForms, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = expenseFormService.queryPage(expenseFormQuery, ExpenseForm.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(ExpenseFormQuery expenseFormQuery) {

    }

    @ApiOperation("根据id获取报销单")
    @GetMapping("/{expenseFormId}")
    @RequiresPermissions("expense:expenseForm:get")
    public Result findById(@PathVariable("expenseFormId") String expenseFormId, FieldsView fieldsView) {
        ExpenseForm expenseForm = expenseFormService.findById(expenseFormId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation("删除报销单")
    @DeleteMapping("/{expenseFormId}")
    @RequiresPermissions("expense:expenseForm:delete")
    public Result delete(@PathVariable("expenseFormId") String expenseFormId) {
        expenseFormService.delete(expenseFormId);
        return Result.getSuccess();
    }

}

package com.xiaowei.expensereimbursementweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ExpenseAccount;
import com.xiaowei.expensereimbursement.service.IExpenseAccountService;
import com.xiaowei.expensereimbursementweb.dto.ExpenseAccountDTO;
import com.xiaowei.expensereimbursementweb.query.ExpenseAccountQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 二级费用科目管理
 */
@Api(tags = "二级费用科目接口")
@RestController
@RequestMapping("/api/expenseaccount")
public class ExpenseAccountController {

    @Autowired
    private IExpenseAccountService expenseAccountService;

    @ApiOperation(value = "添加二级费用科目")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) ExpenseAccountDTO expenseAccountDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseAccount expenseAccount = BeanCopyUtils.copy(expenseAccountDTO, ExpenseAccount.class);
        expenseAccount.setAccountContent(JSONArray.toJSONString(expenseAccountDTO.getAccountContentBeans()));
        expenseAccount = expenseAccountService.saveExpenseAccount(expenseAccount);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseAccount, fieldsView));
    }


    @ApiOperation(value = "修改二级费用科目")
    @AutoErrorHandler
    @PutMapping("/{accountId}")
    public Result update(@PathVariable("accountId") String accountId, @RequestBody @Validated(V.Update.class) ExpenseAccountDTO expenseAccountDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseAccount expenseAccount = BeanCopyUtils.copy(expenseAccountDTO, ExpenseAccount.class);
        expenseAccount.setAccountContent(JSONArray.toJSONString(expenseAccountDTO.getAccountContentBeans()));
        expenseAccount.setId(accountId);
        expenseAccount = expenseAccountService.updateExpenseAccount(expenseAccount);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseAccount, fieldsView));
    }

    @ApiOperation("二级费用科目查询接口")
    @GetMapping("")
    public Result query(ExpenseAccountQuery expenseAccountQuery, FieldsView fieldsView) {
        if (expenseAccountQuery.isNoPage()) {
            List<ExpenseAccount> expenseAccounts = expenseAccountService.query(expenseAccountQuery, ExpenseAccount.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(expenseAccounts, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = expenseAccountService.queryPage(expenseAccountQuery, ExpenseAccount.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    @ApiOperation("根据id获取二级费用科目")
    @GetMapping("/{accountId}")
    public Result findById(@PathVariable("accountId") String accountId, FieldsView fieldsView) {
        ExpenseAccount expenseAccount = expenseAccountService.findById(accountId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseAccount, fieldsView));
    }

}

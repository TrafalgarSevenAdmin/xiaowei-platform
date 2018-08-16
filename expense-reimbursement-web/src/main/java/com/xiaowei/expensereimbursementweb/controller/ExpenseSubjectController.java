package com.xiaowei.expensereimbursementweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.tree.JsonTreeCreater;
import com.xiaowei.core.tree.JsonTreeData;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import com.xiaowei.expensereimbursement.service.IExpenseSubjectService;
import com.xiaowei.expensereimbursementweb.dto.ExpenseSubjectDTO;
import com.xiaowei.expensereimbursementweb.query.ExpenseSubjectQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用科目管理
 */
@Api(tags = "费用科目接口")
@RestController
@RequestMapping("/api/expensesubject")
public class ExpenseSubjectController {

    @Autowired
    private IExpenseSubjectService expenseSubjectService;

    @ApiOperation(value = "添加费用科目")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) ExpenseSubjectDTO expenseSubjectDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseSubject expenseSubject = BeanCopyUtils.copy(expenseSubjectDTO, ExpenseSubject.class);
        expenseSubject.setAccountContent(JSONArray.toJSONString(expenseSubjectDTO.getAccountContentBeans()));
        expenseSubject = expenseSubjectService.saveExpenseSubject(expenseSubject);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseSubject, fieldsView));
    }


    @ApiOperation(value = "修改费用科目")
    @AutoErrorHandler
    @PutMapping("/{subjectId}")
    public Result update(@PathVariable("subjectId") String subjectId, @RequestBody @Validated(V.Update.class) ExpenseSubjectDTO expenseSubjectDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseSubject expenseSubject = BeanCopyUtils.copy(expenseSubjectDTO, ExpenseSubject.class);
        expenseSubject.setAccountContent(JSONArray.toJSONString(expenseSubjectDTO.getAccountContentBeans()));
        expenseSubject.setId(subjectId);
        expenseSubject = expenseSubjectService.updateExpenseSubject(expenseSubject);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseSubject, fieldsView));
    }

    @ApiOperation("费用科目查询接口")
    @GetMapping("")
    public Result query(ExpenseSubjectQuery expenseSubjectQuery, FieldsView fieldsView) {
        if (expenseSubjectQuery.isNoPage()) {
            List<ExpenseSubject> expenseSubjects = expenseSubjectService.query(expenseSubjectQuery, ExpenseSubject.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(expenseSubjects, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = expenseSubjectService.queryPage(expenseSubjectQuery, ExpenseSubject.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    @ApiOperation("费用科目树结构查询接口")
    @GetMapping("/tree")
    public Result tree() {
        final List<JsonTreeData> jsonTreeData = new JsonTreeCreater<ExpenseSubject>(expenseSubjectService.findAll(),
                item -> item.getId(),
                a -> StringUtils.isEmpty(a.getParentId()) ? "0" : a.getParentId(),
                a -> a.getSubjectName(),
                a -> false,
                a -> a,
                a -> false
        ).create();
        return Result.getSuccess(jsonTreeData);
    }

    @ApiOperation("根据id获取费用科目")
    @GetMapping("/{subjectId}")
    public Result findById(@PathVariable("subjectId") String subjectId, FieldsView fieldsView) {
        ExpenseSubject expenseSubject = expenseSubjectService.findById(subjectId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseSubject, fieldsView));
    }

}

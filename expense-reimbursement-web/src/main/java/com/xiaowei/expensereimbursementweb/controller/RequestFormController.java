package com.xiaowei.expensereimbursementweb.controller;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.RequestForm;
import com.xiaowei.expensereimbursement.service.IRequestFormService;
import com.xiaowei.expensereimbursement.status.RequestFormStatus;
import com.xiaowei.expensereimbursementweb.dto.RequestFormDTO;
import com.xiaowei.expensereimbursementweb.query.RequestFormQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用申请管理
 */
@Api(tags = "费用申请接口")
@RestController
@RequestMapping("/api/RequestForm")
public class RequestFormController {

    @Autowired
    private IRequestFormService requestFormService;

    @ApiOperation(value = "添加费用申请")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:requestForm:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) RequestFormDTO requestFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm = requestFormService.saveRequestForm(requestForm);
        if(requestForm.getStatus().equals(RequestFormStatus.PREAUDIT.getStatus())){
            //微信推送给审核人
        }
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    @ApiOperation(value = "修改费用申请")
    @AutoErrorHandler
    @PutMapping("/{requestFormId}")
    @RequiresPermissions("expense:requestForm:update")
    public Result update(@PathVariable("requestFormId") String requestFormId, @RequestBody @Validated(V.Update.class) RequestFormDTO requestFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm.setId(requestFormId);
        requestForm = requestFormService.updateRequestForm(requestForm);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    @ApiOperation(value = "费用申请审核")
    @AutoErrorHandler
    @PutMapping("/{requestFormId}/audit")
    @RequiresPermissions("expense:requestForm:audit")
    public Result firstAudit(@PathVariable("requestFormId") String requestFormId,
                             @RequestBody @Validated(RequestFormDTO.Audit.class) RequestFormDTO requestFormDTO,
                             BindingResult bindingResult,
                             @RequestParam Boolean audit,
                             FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm.setId(requestFormId);
        requestForm = requestFormService.audit(requestForm, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    @ApiOperation("费用申请查询接口")
    @GetMapping("")
    @RequiresPermissions("expense:requestForm:query")
    public Result query(RequestFormQuery requestFormQuery, FieldsView fieldsView) {
        //查询费用申请设置默认条件
        setDefaultCondition(requestFormQuery);

        if (requestFormQuery.isNoPage()) {
            List<RequestForm> requestForms = requestFormService.query(requestFormQuery, RequestForm.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(requestForms, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = requestFormService.queryPage(requestFormQuery, RequestForm.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(RequestFormQuery requestFormQuery) {

    }

    @ApiOperation("根据id获取费用申请")
    @GetMapping("/{requestFormId}")
    @RequiresPermissions("expense:requestForm:get")
    public Result findById(@PathVariable("requestFormId") String requestFormId, FieldsView fieldsView) {
        RequestForm requestForm = requestFormService.findById(requestFormId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    @ApiOperation("删除费用申请")
    @DeleteMapping("/{requestFormId}")
    @RequiresPermissions("expense:requestForm:delete")
    public Result delete(@PathVariable("requestFormId") String requestFormId) {
        requestFormService.delete(requestFormId);
        return Result.getSuccess();
    }

}

package com.xiaowei.expensereimbursementweb.controller;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;
import com.xiaowei.expensereimbursement.service.IExpenseFormService;
import com.xiaowei.expensereimbursement.status.ExpenseFormStatus;
import com.xiaowei.expensereimbursementweb.dto.ExpenseFormDTO;
import com.xiaowei.expensereimbursementweb.query.ExpenseFormQuery;
import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MessageType;
import com.xiaowei.mq.sender.MessagePushSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报销单管理
 */
@Api(tags = "报销单接口")
@RestController
@RequestMapping("/api/expenseForm")
public class ExpenseFormController {

    @Autowired
    private IExpenseFormService expenseFormService;
    @Autowired
    private MessagePushSender messagePushSender;
    @Value("${server.host}")
    private String serverHost;

    @ApiOperation(value = "添加报销单")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:expenseForm:add")
    @HandleLog(type = "添加报销单", contentParams = {@ContentParam(useParamField = true, field = "expenseFormDTO", value = "报销单信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) ExpenseFormDTO expenseFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm = expenseFormService.saveExpenseForm(expenseForm);
        if (expenseForm.getStatus().equals(ExpenseFormStatus.PREAUDIT.getStatus())) {
            preliminaryNotification(expenseForm);
        }
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation(value = "修改报销单")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}")
    @RequiresPermissions("expense:expenseForm:update")
    @HandleLog(type = "修改报销单", contentParams = {@ContentParam(useParamField = true, field = "expenseFormDTO", value = "报销单信息"),
            @ContentParam(useParamField = false, field = "expenseFormId", value = "报销单id")})
    public Result update(@PathVariable("expenseFormId") String expenseFormId, @RequestBody @Validated(V.Update.class) ExpenseFormDTO expenseFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.updateExpenseForm(expenseForm);
        if (expenseForm.getStatus().equals(ExpenseFormStatus.PREAUDIT.getStatus())) {
            preliminaryNotification(expenseForm);
        }
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation(value = "报销单初审")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}/first")
    @RequiresPermissions("expense:expenseForm:first")
    @HandleLog(type = "报销单初审", contentParams = {@ContentParam(useParamField = true, field = "expenseFormDTO", value = "报销单信息"),
            @ContentParam(useParamField = false, field = "expenseFormId", value = "报销单id")})
    public Result firstAudit(@PathVariable("expenseFormId") String expenseFormId,
                             @RequestBody @Validated(ExpenseFormDTO.FirstAudit.class) ExpenseFormDTO expenseFormDTO,
                             BindingResult bindingResult,
                             @RequestParam Boolean audit,
                             FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.firstAudit(expenseForm, audit);
        if (audit) {
            reviewNotification(expenseForm);
        }
        noticeOfPreliminaryExaminationResult(expenseForm, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    /**
     * 初审通知
     *
     * @param expenseForm
     */
    private void preliminaryNotification(ExpenseForm expenseForm) {
        try {
            expenseForm.getFirstTrials().stream().forEach(sysUser -> {
                UserMessageBean userMessageBean = new UserMessageBean();
                userMessageBean.setUserId(sysUser.getId());
                userMessageBean.setMessageType(MessageType.EXPENSEAUDITNOTICE);
                Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
                messageMap.put("first", new UserMessageBean.Payload("费用报销单待初审", null));
                //待审核单号：
                messageMap.put("keyword1", new UserMessageBean.Payload(expenseForm.getCode(), null));
                //填报人：
                messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(expenseForm.getExpenseUser().getNickName()) ? expenseForm.getExpenseUser().getNickName() : expenseForm.getExpenseUser().getLoginName(), null));
                //填报时间：
                messageMap.put("keyword3", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expenseForm.getCreatedTime()), null));
                //填报总金额：
                messageMap.put("keyword4", new UserMessageBean.Payload(expenseForm.getFillAmount() + "", null));
                //remark：
                messageMap.put("remark", new UserMessageBean.Payload("请尽快完成初审任务!", null));
                userMessageBean.setData(messageMap);
                userMessageBean.setUrl(serverHost + "/xwkx-web/expense/approvalList");
                messagePushSender.sendWxMessage(userMessageBean);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初审结果通知
     *
     * @param expenseForm
     */
    private void noticeOfPreliminaryExaminationResult(ExpenseForm expenseForm, Boolean audit) {
        try {
            UserMessageBean userMessageBean = new UserMessageBean();
            userMessageBean.setUserId(expenseForm.getExpenseUser().getId());
            userMessageBean.setMessageType(MessageType.NOTIFICATIONOFAUDITRESULTS);
            Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
            messageMap.put("first", new UserMessageBean.Payload("费用报销单初审结果", null));
            //审核单号：
            messageMap.put("keyword1", new UserMessageBean.Payload(expenseForm.getCode(), null));
            //审核人：
            messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(expenseForm.getFirstAudit().getNickName()) ? expenseForm.getFirstAudit().getNickName() : expenseForm.getFirstAudit().getLoginName(), null));
            //审核结果：
            messageMap.put("keyword3", new UserMessageBean.Payload(audit ? "已通过" : "未通过", null));
            //审核时间：
            messageMap.put("keyword4", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expenseForm.getFirstAuditTime()), null));
            //审核意见：
            messageMap.put("keyword5", new UserMessageBean.Payload(expenseForm.getFirstOption(), null));
            //remark：
            messageMap.put("remark", new UserMessageBean.Payload("请修改并重新发起审核请求!", null));
            userMessageBean.setData(messageMap);
            userMessageBean.setUrl(serverHost + "/xwkx-web/expense/expenseList");
            messagePushSender.sendWxMessage(userMessageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复审结果通知
     *
     * @param expenseForm
     */
    private void notificationOfReviewResult(ExpenseForm expenseForm, Boolean audit) {
        try {
            UserMessageBean userMessageBean = new UserMessageBean();
            userMessageBean.setUserId(expenseForm.getExpenseUser().getId());
            userMessageBean.setMessageType(MessageType.NOTIFICATIONOFAUDITRESULTS);
            Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
            messageMap.put("first", new UserMessageBean.Payload("费用报销单复审结果", null));
            //审核单号：
            messageMap.put("keyword1", new UserMessageBean.Payload(expenseForm.getCode(), null));
            //审核人：
            messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(expenseForm.getSecondAudit().getNickName()) ? expenseForm.getSecondAudit().getNickName() : expenseForm.getSecondAudit().getLoginName(), null));
            //审核结果：
            messageMap.put("keyword3", new UserMessageBean.Payload(audit ? "已通过" : "未通过", null));
            //审核时间：
            messageMap.put("keyword4", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expenseForm.getSecondAuditTime()), null));
            //审核意见：
            messageMap.put("keyword5", new UserMessageBean.Payload(expenseForm.getSecondOption(), null));
            //remark：
            messageMap.put("remark", new UserMessageBean.Payload("请修改并重新发起审核请求!", null));
            userMessageBean.setData(messageMap);
            userMessageBean.setUrl(serverHost + "/xwkx-web/expense/expenseList");
            messagePushSender.sendWxMessage(userMessageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复审通知
     *
     * @param expenseForm
     */
    private void reviewNotification(ExpenseForm expenseForm) {
        try {
            expenseForm.getSecondTrials().stream().forEach(sysUser -> {
                UserMessageBean userMessageBean = new UserMessageBean();
                userMessageBean.setUserId(sysUser.getId());
                userMessageBean.setMessageType(MessageType.EXPENSEAUDITNOTICE);
                Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
                messageMap.put("first", new UserMessageBean.Payload("费用报销单待复审", null));
                //待审核单号：
                messageMap.put("keyword1", new UserMessageBean.Payload(expenseForm.getCode(), null));
                //填报人：
                messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(expenseForm.getExpenseUser().getNickName()) ? expenseForm.getExpenseUser().getNickName() : expenseForm.getExpenseUser().getLoginName(), null));
                //填报时间：
                messageMap.put("keyword3", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expenseForm.getCreatedTime()), null));
                //填报总金额：
                messageMap.put("keyword4", new UserMessageBean.Payload(expenseForm.getFillAmount() + "", null));
                //remark：
                messageMap.put("remark", new UserMessageBean.Payload("请尽快完成复审任务!", null));
                userMessageBean.setData(messageMap);
                userMessageBean.setUrl(serverHost + "/xwkx-web/expense/approvalList");
                messagePushSender.sendWxMessage(userMessageBean);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "报销单复审")
    @AutoErrorHandler
    @PutMapping("/{expenseFormId}/second")
    @RequiresPermissions("expense:expenseForm:second")
    @HandleLog(type = "报销单复审", contentParams = {@ContentParam(useParamField = true, field = "expenseFormDTO", value = "报销单信息"),
            @ContentParam(useParamField = false, field = "expenseFormId", value = "报销单id")})
    public Result secondAudit(@PathVariable("expenseFormId") String expenseFormId,
                              @RequestBody @Validated(ExpenseFormDTO.SecondAudit.class) ExpenseFormDTO expenseFormDTO,
                              BindingResult bindingResult,
                              @RequestParam Boolean audit,
                              FieldsView fieldsView) throws Exception {
        ExpenseForm expenseForm = BeanCopyUtils.copy(expenseFormDTO, ExpenseForm.class);
        expenseForm.setId(expenseFormId);
        expenseForm = expenseFormService.secondAudit(expenseForm, audit);
        notificationOfReviewResult(expenseForm, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(expenseForm, fieldsView));
    }

    @ApiOperation("审核人查询报销单各种状态数量")
    @GetMapping("/audit/count")
    @RequiresPermissions("expense:expenseForm:auditCount")
    public Result auditCount() {
        final String userId = LoginUserUtils.getLoginUser().getId();
        return Result.getSuccess(expenseFormService.auditCountByUserId(userId));
    }

    @ApiOperation("报销人查询报销单各种状态数量")
    @GetMapping("/expenser/count")
    @RequiresPermissions("expense:expenseForm:expenserCount")
    public Result expenserCount() {
        final String userId = LoginUserUtils.getLoginUser().getId();
        return Result.getSuccess(expenseFormService.expenserCount(userId));
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
    @HandleLog(type = "删除报销单", contentParams = {@ContentParam(useParamField = false, field = "expenseFormId", value = "报销单id")})
    public Result delete(@PathVariable("expenseFormId") String expenseFormId) {
        expenseFormService.delete(expenseFormId);
        return Result.getSuccess();
    }

}

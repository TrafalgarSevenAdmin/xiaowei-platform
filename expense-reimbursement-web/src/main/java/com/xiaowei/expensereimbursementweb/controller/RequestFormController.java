package com.xiaowei.expensereimbursementweb.controller;

import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
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
import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MessageType;
import com.xiaowei.mq.sender.MessagePushSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用申请管理
 */
@Api(tags = "费用申请接口")
@RestController
@RequestMapping("/api/RequestForm")
public class RequestFormController {

    @Autowired
    private IRequestFormService requestFormService;
    @Autowired
    private MessagePushSender messagePushSender;

    @ApiOperation(value = "添加费用申请")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:requestForm:add")
    @HandleLog(type = "添加费用申请", contentParams = {@ContentParam(useParamField = true, field = "requestFormDTO", value = "申请单信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) RequestFormDTO requestFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm = requestFormService.saveRequestForm(requestForm);
        if(requestForm.getStatus().equals(RequestFormStatus.PREAUDIT.getStatus())){
            //微信推送给审核人
            auditNotification(requestForm);
        }
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    @ApiOperation(value = "修改费用申请")
    @AutoErrorHandler
    @PutMapping("/{requestFormId}")
    @RequiresPermissions("expense:requestForm:update")
    @HandleLog(type = "修改费用申请", contentParams = {@ContentParam(useParamField = true, field = "requestFormDTO", value = "申请单信息"),
            @ContentParam(useParamField = false, field = "requestFormId", value = "申请单id")})
    public Result update(@PathVariable("requestFormId") String requestFormId, @RequestBody @Validated(V.Update.class) RequestFormDTO requestFormDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm.setId(requestFormId);
        requestForm = requestFormService.updateRequestForm(requestForm);
        if(requestForm.getStatus().equals(RequestFormStatus.PREAUDIT.getStatus())){
            //微信推送给审核人
            auditNotification(requestForm);
        }
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestForm, fieldsView));
    }

    /**
     * 审核通知
     *
     * @param requestForm
     */
    private void auditNotification(RequestForm requestForm) {
        try {
            requestForm.getTrials().stream().forEach(sysUser -> {
                UserMessageBean userMessageBean = new UserMessageBean();
                userMessageBean.setUserId(sysUser.getId());
                userMessageBean.setMessageType(MessageType.EXPENSEAUDITNOTICE);
                Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
                messageMap.put("first", new UserMessageBean.Payload("费用申请单待审核", null));
                //待审核单号：
                messageMap.put("keyword1", new UserMessageBean.Payload(requestForm.getCode(), null));
                //填报人：
                messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(requestForm.getRequestUser().getNickName()) ? requestForm.getRequestUser().getNickName() : requestForm.getRequestUser().getLoginName(), null));
                //填报时间：
                messageMap.put("keyword3", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestForm.getCreatedTime()), null));
                //填报总金额：
                messageMap.put("keyword4", new UserMessageBean.Payload(requestForm.getFillAmount() + "", null));
                //remark：
                messageMap.put("remark", new UserMessageBean.Payload("请尽快完成审核任务!", null));
                userMessageBean.setData(messageMap);
                messagePushSender.sendWxMessage(userMessageBean);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核结果通知
     *
     * @param requestForm
     */
    private void notificationOfAuditResults(RequestForm requestForm, Boolean audit) {
        try {
            UserMessageBean userMessageBean = new UserMessageBean();
            userMessageBean.setUserId(requestForm.getRequestUser().getId());
            userMessageBean.setMessageType(MessageType.NOTIFICATIONOFAUDITRESULTS);
            Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
            messageMap.put("first", new UserMessageBean.Payload("费用申请单审核结果", null));
            //审核单号：
            messageMap.put("keyword1", new UserMessageBean.Payload(requestForm.getCode(), null));
            //审核人：
            messageMap.put("keyword2", new UserMessageBean.Payload(StringUtils.isNotEmpty(requestForm.getAudit().getNickName()) ? requestForm.getAudit().getNickName() : requestForm.getAudit().getLoginName(), null));
            //审核结果：
            messageMap.put("keyword3", new UserMessageBean.Payload(audit ? "已通过" : "未通过", null));
            //审核时间：
            messageMap.put("keyword4", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestForm.getAuditTime()), null));
            //审核意见：
            messageMap.put("keyword5", new UserMessageBean.Payload(requestForm.getOption(), null));
            //remark：
            messageMap.put("remark", new UserMessageBean.Payload("请修改并重新发起审核请求!", null));
            userMessageBean.setData(messageMap);
            messagePushSender.sendWxMessage(userMessageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "费用申请审核")
    @AutoErrorHandler
    @PutMapping("/{requestFormId}/audit")
    @RequiresPermissions("expense:requestForm:audit")
    @HandleLog(type = "费用申请审核", contentParams = {@ContentParam(useParamField = true, field = "requestFormDTO", value = "申请单信息"),
            @ContentParam(useParamField = false, field = "requestFormId", value = "申请单id")})
    public Result audit(@PathVariable("requestFormId") String requestFormId,
                             @RequestBody @Validated(RequestFormDTO.Audit.class) RequestFormDTO requestFormDTO,
                             BindingResult bindingResult,
                             @RequestParam Boolean audit,
                             FieldsView fieldsView) throws Exception {
        RequestForm requestForm = BeanCopyUtils.copy(requestFormDTO, RequestForm.class);
        requestForm.setId(requestFormId);
        requestForm = requestFormService.audit(requestForm, audit);
        notificationOfAuditResults(requestForm, audit);
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
    @HandleLog(type = "删除费用申请", contentParams = {@ContentParam(useParamField = false, field = "requestFormId", value = "申请单id")})
    public Result delete(@PathVariable("requestFormId") String requestFormId) {
        requestFormService.delete(requestFormId);
        return Result.getSuccess();
    }

}

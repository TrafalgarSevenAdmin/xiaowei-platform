package com.xiaowei.worksystem.controller;

import com.xiaowei.account.entity.AuditConfiguration;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.IAuditConfigurationService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.status.AuditTypeStatus;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MessageType;
import com.xiaowei.mq.sender.MessagePushSender;
import com.xiaowei.worksystem.dto.RequestWorkOrderDTO;
import com.xiaowei.worksystem.entity.RequestWorkOrder;
import com.xiaowei.worksystem.query.RequestWorkOrderQuery;
import com.xiaowei.worksystem.service.IRequestWorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工单请求管理
 */
@Api(tags = "工单请求接口")
@RestController
@RequestMapping("/api/request")
public class RequestWorkOrderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IRequestWorkOrderService requestWorkOrderService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IAuditConfigurationService auditConfigurationService;
    @Autowired
    private MessagePushSender messagePushSender;

    @Value("${server.host}")
    private String serverHost;

    @ApiOperation(value = "添加工单请求")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:request:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) RequestWorkOrderDTO requestWorkOrderDTO,
                         BindingResult bindingResult,
                         FieldsView fieldsView) throws Exception {
        RequestWorkOrder requestWorkOrder = BeanCopyUtils.copy(requestWorkOrderDTO, RequestWorkOrder.class);
        requestWorkOrder.setShape(GeometryUtil.transWKT(requestWorkOrderDTO.getWkt()));
        requestWorkOrder = requestWorkOrderService.saveRequestWorkOrder(requestWorkOrder);
        //需要派单员派单的通知
        messageToSendorder(requestWorkOrder);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestWorkOrder, fieldsView));
    }


    /**
     * 需要派单员派单的通知
     *
     * @param requestWorkOrder
     */
    private void messageToSendorder(RequestWorkOrder requestWorkOrder) {
        //查询派单员
        Set<String> userIds = auditConfigurationService.findByType(AuditTypeStatus.SENDORDER.getStatus()).stream().map(AuditConfiguration::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<SysUser> users = userService.findByUserIdIn(userIds);
        for (SysUser user : users) {
            try {
                UserMessageBean userMessageBean = new UserMessageBean();
                userMessageBean.setUserId(user.getId());
                userMessageBean.setMessageType(MessageType.MESSAGETOSENDORDER);
                Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
                messageMap.put("first", new UserMessageBean.Payload("您有新的任务消息通知,请尽快确认处理", null));
                messageMap.put("keyword1", new UserMessageBean.Payload(user.getCompany() != null ? user.getCompany().getCompanyName() : null, null));
                messageMap.put("keyword2", new UserMessageBean.Payload(requestWorkOrder.getLinkMan(), null));
                messageMap.put("keyword3", new UserMessageBean.Payload(requestWorkOrder.getLinkPhone(), null));
                userMessageBean.setData(messageMap);
                userMessageBean.setUrl(serverHost + "/xwkx-web/management/manRequestHandle?requestId=" + requestWorkOrder.getId());
                messagePushSender.sendWxMessage(userMessageBean);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                e.printStackTrace();
            }
        }

    }

    @ApiOperation("工单请求查询接口")
    @GetMapping("")
    @RequiresPermissions("order:request:query")
    public Result query(RequestWorkOrderQuery requestWorkOrderQuery, FieldsView fieldsView) {
        //查询工单请求设置默认条件
        setDefaultCondition(requestWorkOrderQuery);

        if (requestWorkOrderQuery.isNoPage()) {
            List<RequestWorkOrder> requestWorkOrders = requestWorkOrderService.query(requestWorkOrderQuery, RequestWorkOrder.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(requestWorkOrders, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = requestWorkOrderService.queryPage(requestWorkOrderQuery, RequestWorkOrder.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(RequestWorkOrderQuery requestWorkOrderQuery) {
    }

    @ApiOperation("根据id获取工单请求")
    @GetMapping("/{requestWorkOrderId}")
    @RequiresPermissions("order:request:get")
    public Result findById(@PathVariable("requestWorkOrderId") String requestWorkOrderId, FieldsView fieldsView) {
        RequestWorkOrder requestWorkOrder = requestWorkOrderService.findById(requestWorkOrderId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestWorkOrder, fieldsView));
    }

    @ApiOperation("工单请求")
    @PutMapping("/{requestWorkOrderId}/status")
    @RequiresPermissions("order:request:status")
    public Result updateStatus(@PathVariable("requestWorkOrderId") String requestWorkOrderId,
                               @RequestBody @Validated(RequestWorkOrderDTO.UpdateStatus.class) RequestWorkOrderDTO requestWorkOrderDTO,
                               BindingResult bindingResult,
                               FieldsView fieldsView) {
        RequestWorkOrder requestWorkOrder = BeanCopyUtils.copy(requestWorkOrderDTO, RequestWorkOrder.class);
        requestWorkOrder.setId(requestWorkOrderId);
        requestWorkOrder = requestWorkOrderService.updateStatus(requestWorkOrder);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestWorkOrder, fieldsView));
    }

}

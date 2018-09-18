package com.xiaowei.worksystem.controller;

import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
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
import com.xiaowei.worksystem.dto.ExecuteServiceItemDTO;
import com.xiaowei.worksystem.dto.ServiceItemDTO;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.query.ServiceItemQuery;
import com.xiaowei.worksystem.service.IServiceItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维修项目管理
 */
@Api(tags = "维修项目接口")
@RestController
@RequestMapping("/api/serviceitem")
public class ServiceItemController {

    /**
     * 设备服务
     */
    @Autowired
    private IServiceItemService serviceItemService;
    @Autowired
    private MessagePushSender messagePushSender;
    @Value("${server.host}")
    private String serverHost;

    @ApiOperation(value = "工程师添加收费项目")
    @AutoErrorHandler
    @PostMapping("/{workOrderId}/engineer")
    @RequiresPermissions("order:serviceitem:add")
    @HandleLog(type = "工程师添加收费项目", contentParams = {@ContentParam(useParamField = true, field = "serviceItemDTOs", value = "项目信息"),
            @ContentParam(useParamField = false, field = "workOrderId", value = "工单id")})
    public Result insertByEngineer(@PathVariable("workOrderId") String workOrderId, @RequestBody @Validated(V.Insert.class) List<ServiceItemDTO> serviceItemDTOs, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        List<ServiceItem> serviceItems = BeanCopyUtils.copyList(serviceItemDTOs, ServiceItem.class);
        serviceItems = serviceItemService.saveByEngineer(workOrderId, serviceItems);
        for (int i = 0; i < serviceItems.size(); i++) {
            ServiceItem serviceItem = serviceItems.get(i);
            if (serviceItem.getCharge()) {
                processingNotification(serviceItem.getWorkOrder(), "待确认");
                break;
            }
        }
        return Result.getSuccess(ObjectToMapUtils.listToMap(serviceItems, fieldsView));
    }

    /**
     * 工单处理通知
     *
     * @param workOrder
     * @param status
     */
    private void processingNotification(WorkOrder workOrder, String status) {
        try {
            UserMessageBean userMessageBean = new UserMessageBean();
            userMessageBean.setUserId(workOrder.getProposer().getId());
            userMessageBean.setMessageType(MessageType.PROCESSINGNOTIFICATION);
            Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
            messageMap.put("first", new UserMessageBean.Payload("您的工单新增收费项目,请尽快确认", null));
            messageMap.put("keyword1", new UserMessageBean.Payload(workOrder.getCode(), null));
            messageMap.put("keyword2", new UserMessageBean.Payload(workOrder.getWorkOrderType().getName(), null));
            messageMap.put("keyword3", new UserMessageBean.Payload(status, null));
            messageMap.put("keyword4", new UserMessageBean.Payload(workOrder.getEngineer().getNickName(), null));
            userMessageBean.setData(messageMap);
            userMessageBean.setUrl(serverHost + "/xwkx-web/user/orderConfirm?orderId=" + workOrder.getId());
            messagePushSender.sendWxMessage(userMessageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "工程师执行服务项目")
    @PutMapping("/{serviceItemId}/execute")
    @RequiresPermissions("order:serviceitem:execute")
    @HandleLog(type = "工程师添加收费项目", contentParams = {@ContentParam(useParamField = true, field = "executeServiceItemDTO", value = "执行信息"),
            @ContentParam(useParamField = false, field = "serviceItemId", value = "项目id")})
    public Result executeServiceItem(@PathVariable("serviceItemId") String serviceItemId, ExecuteServiceItemDTO executeServiceItemDTO, FieldsView fieldsView) throws Exception {
        ServiceItem serviceItem = serviceItemService.executeServiceItem(serviceItemId,
                executeServiceItemDTO.getQualityFileStore(),
                executeServiceItemDTO.getEndingState());
        return Result.getSuccess(ObjectToMapUtils.objectToMap(serviceItem, fieldsView));
    }

    @ApiOperation(value = "质检服务项目")
    @PutMapping("/{serviceItemId}/quality")
    @RequiresPermissions("order:serviceitem:quality")
    @HandleLog(type = "工程师添加收费项目", contentParams = {@ContentParam(useParamField = false, field = "audit", value = "是否通过"),
            @ContentParam(useParamField = false, field = "serviceItemId", value = "项目id")})
    public Result qualityServiceItem(@PathVariable("serviceItemId") String serviceItemId, @RequestBody Boolean audit, FieldsView fieldsView) throws Exception {
        ServiceItem serviceItem = serviceItemService.qualityServiceItem(serviceItemId, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(serviceItem, fieldsView));
    }

    @ApiOperation("服务项目查询接口")
    @GetMapping("")
    @RequiresPermissions("order:serviceitem:query")
    public Result query(ServiceItemQuery serviceItemQuery, FieldsView fieldsView) {
        //查询服务项目设置默认条件
        setDefaultCondition(serviceItemQuery);

        if (serviceItemQuery.isNoPage()) {
            List<ServiceItem> serviceItems = serviceItemService.query(serviceItemQuery, ServiceItem.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(serviceItems, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = serviceItemService.queryPage(serviceItemQuery, ServiceItem.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(ServiceItemQuery serviceItemQuery) {

    }


}

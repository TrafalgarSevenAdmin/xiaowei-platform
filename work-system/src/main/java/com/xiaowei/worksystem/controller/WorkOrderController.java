package com.xiaowei.worksystem.controller;

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
import com.xiaowei.worksystem.dto.EvaluateDTO;
import com.xiaowei.worksystem.dto.WorkOrderDTO;
import com.xiaowei.worksystem.entity.Evaluate;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.query.WorkOrderQuery;
import com.xiaowei.worksystem.service.IEvaluateService;
import com.xiaowei.worksystem.service.IWorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单管理
 */
@Api(tags = "工单接口")
@RestController
@RequestMapping("/api/workorder")
public class WorkOrderController {

    @Autowired
    private IWorkOrderService workOrderService;
    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private MessagePushSender messagePushSender;

    @ApiOperation(value = "添加工单")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) WorkOrderDTO workOrderDTO,
                         BindingResult bindingResult,
                         String workFlowId,
                         FieldsView fieldsView) throws Exception {
        WorkOrder workOrder = BeanCopyUtils.copy(workOrderDTO, WorkOrder.class);
        workOrder = workOrderService.saveWorkOrder(workOrder, workFlowId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrder, fieldsView));
    }

    @ApiOperation(value = "添加评价")
    @AutoErrorHandler
    @PostMapping("/{workOrderId}/evaluate")
    public Result insertEvaluate(@PathVariable("workOrderId") String workOrderId, @RequestBody @Validated(V.Insert.class) EvaluateDTO evaluateDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Evaluate evaluate = BeanCopyUtils.copy(evaluateDTO, Evaluate.class);
        evaluate = evaluateService.saveEvaluate(workOrderId, evaluate);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(evaluate, fieldsView));
    }

    @ApiOperation(value = "修改工单")
    @AutoErrorHandler
    @PutMapping("/{workOrderId}")
    public Result update(@PathVariable("workOrderId") String workOrderId,
                         @RequestBody @Validated(V.Update.class) WorkOrderDTO workOrderDTO,
                         BindingResult bindingResult,
                         String workFlowId,
                         FieldsView fieldsView) throws Exception {
        WorkOrder workOrder = BeanCopyUtils.copy(workOrderDTO, WorkOrder.class);
        workOrder.setId(workOrderId);
        workOrder = workOrderService.updateWorkOrder(workOrder, workFlowId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrder, fieldsView));
    }

    @ApiOperation(value = "用户确认项目")
    @AutoErrorHandler
    @PutMapping("/confirmed/{workOrderId}")
    public Result confirmedServiceItem(@PathVariable("workOrderId") String workOrderId, @RequestBody List<String> serviceItemIds, FieldsView fieldsView) throws Exception {
        workOrderService.confirmed(workOrderId, serviceItemIds);
        return Result.getSuccess();
    }

    @ApiOperation(value = "派单")
    @AutoErrorHandler
    @PutMapping("/distribute/{workOrderId}")
    public Result distributeWorkOrder(@PathVariable("workOrderId") String workOrderId,
                                      @RequestBody @Validated(WorkOrderDTO.DistributeWorkOrder.class) WorkOrderDTO workOrderDTO,
                                      BindingResult bindingResult,
                                      String workFlowId, FieldsView fieldsView) throws Exception {
        WorkOrder workOrder = BeanCopyUtils.copy(workOrderDTO, WorkOrder.class);
        workOrder.setId(workOrderId);
        workOrder = workOrderService.distributeWorkOrder(workOrder, workFlowId);
        //派单提醒通知
        maintenanceOfDispatching(workOrder);
        return Result.getSuccess();
    }

    /**
     * 派单提醒通知
     *
     * @param workOrder
     */
    private void maintenanceOfDispatching(WorkOrder workOrder) {
        try {
            UserMessageBean userMessageBean = new UserMessageBean();
            userMessageBean.setUserId(workOrder.getEngineer().getId());
            userMessageBean.setUrl("www.baidu.com");
            userMessageBean.setMessageType(MessageType.MAINTENANCEOFDISPATCHING);
            Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
            messageMap.put("firtst", new UserMessageBean.Payload("您有新的派单通知,请尽快确认", null));
            messageMap.put("keyword1", new UserMessageBean.Payload(workOrder.getCode(), null));
            messageMap.put("keyword2", new UserMessageBean.Payload(workOrder.getErrorDescription(), null));
            messageMap.put("keyword3", new UserMessageBean.Payload(new SimpleDateFormat("HH:mm:ss").format(workOrder.getCreatedTime()), null));
            messageMap.put("keyword4", new UserMessageBean.Payload(new SimpleDateFormat("yyyy-MM-dd").format(workOrder.getCreatedTime()), null));
            messageMap.put("keyword5", new UserMessageBean.Payload(workOrder.getEquipment().getAddress(), null));
            userMessageBean.setData(messageMap);
            messagePushSender.sendWxMessage(userMessageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "用户付费项目")
    @AutoErrorHandler
    @PutMapping("/pay/{workOrderId}")
    public Result payServiceItem(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) throws Exception {
        workOrderService.payServiceItem(workOrderId);
        return Result.getSuccess();
    }

    @ApiOperation(value = "工程师出发")
    @AutoErrorHandler
    @PutMapping("/departe/{workOrderId}")
    public Result departeWorkOrder(@PathVariable("workOrderId") String workOrderId, @RequestBody String wkt, FieldsView fieldsView) throws Exception {
        workOrderService.departeWorkOrder(workOrderId, GeometryUtil.transWKT(wkt));
        return Result.getSuccess();
    }

    @ApiOperation(value = "工程师开始处理")
    @AutoErrorHandler
    @PutMapping("/inhand/{workOrderId}")
    public Result inhandWorkOrder(@PathVariable("workOrderId") String workOrderId, @RequestBody String wkt, FieldsView fieldsView) throws Exception {
        workOrderService.inhandWorkOrder(workOrderId, GeometryUtil.transWKT(wkt));
        return Result.getSuccess();
    }

    @ApiOperation(value = "工程师处理完成")
    @AutoErrorHandler
    @PutMapping("/finishInhand/{workOrderId}")
    public Result finishInhand(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) throws Exception {
        workOrderService.finishInhand(workOrderId);
        return Result.getSuccess();
    }


    @ApiOperation(value = "工程师接单")
    @AutoErrorHandler
    @PutMapping("/received/{workOrderId}")
    public Result receivedWorkOrder(@PathVariable("workOrderId") String workOrderId, @RequestBody Boolean receive, FieldsView fieldsView) throws Exception {
        workOrderService.receivedWorkOrder(workOrderId, receive);
        return Result.getSuccess();
    }

    @ApiOperation(value = "工单待归档")
    @AutoErrorHandler
    @PutMapping("/prePigeonhole/{workOrderId}")
    public Result prePigeonhole(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) throws Exception {
        workOrderService.prePigeonhole(workOrderId);
        return Result.getSuccess();
    }

    @ApiOperation(value = "工单终审")
    @AutoErrorHandler
    @PutMapping("/pigeonholed/{workOrderId}")
    public Result pigeonholed(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) throws Exception {
        workOrderService.pigeonholed(workOrderId);
        return Result.getSuccess();
    }

    @ApiOperation(value = "工程师预约")
    @AutoErrorHandler
    @PutMapping("/appointing/{workOrderId}")
    public Result appointingWorkOrder(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) throws Exception {
        workOrderService.appointingWorkOrder(workOrderId);
        return Result.getSuccess();
    }

    @ApiOperation("工单查询接口")
    @GetMapping("")
    public Result query(WorkOrderQuery workOrderQuery, FieldsView fieldsView) {
        //查询工单设置默认条件
        setDefaultCondition(workOrderQuery);

        if (workOrderQuery.isNoPage()) {
            List<WorkOrder> workOrders = workOrderService.query(workOrderQuery, WorkOrder.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(workOrders, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = workOrderService.queryPage(workOrderQuery, WorkOrder.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(WorkOrderQuery workOrderQuery) {

    }

    @ApiOperation("根据id获取工单")
    @GetMapping("/{workOrderId}")
    public Result findById(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) {
        WorkOrder workOrder = workOrderService.findById(workOrderId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrder, fieldsView));
    }

    @ApiOperation("删除工单")
    @DeleteMapping("/{workOrderId}")
    public Result delete(@PathVariable("workOrderId") String workOrderId, FieldsView fieldsView) {
        //伪删除
        workOrderService.fakeDelete(workOrderId);
        return Result.getSuccess("删除成功");
    }

}

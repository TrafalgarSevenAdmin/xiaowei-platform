package com.xiaowei.worksystem.controller;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.RequestWorkOrderDTO;
import com.xiaowei.worksystem.entity.RequestWorkOrder;
import com.xiaowei.worksystem.query.RequestWorkOrderQuery;
import com.xiaowei.worksystem.service.IRequestWorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单请求管理
 */
@Api(tags = "工单请求接口")
@RestController
@RequestMapping("/api/request")
public class RequestWorkOrderController {

    @Autowired
    private IRequestWorkOrderService requestWorkOrderService;



    @ApiOperation(value = "添加工单请求")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) RequestWorkOrderDTO requestWorkOrderDTO,
                         BindingResult bindingResult,
                         FieldsView fieldsView) throws Exception {
        RequestWorkOrder requestWorkOrder = BeanCopyUtils.copy(requestWorkOrderDTO, RequestWorkOrder.class);
        requestWorkOrder = requestWorkOrderService.saveRequestWorkOrder(requestWorkOrder);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestWorkOrder, fieldsView));
    }







    @ApiOperation("工单请求查询接口")
    @GetMapping("")
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
    public Result findById(@PathVariable("requestWorkOrderId") String requestWorkOrderId, FieldsView fieldsView) {

        RequestWorkOrder requestWorkOrder = requestWorkOrderService.findById(requestWorkOrderId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(requestWorkOrder, fieldsView));
    }

    @ApiOperation("工单请求")
    @PutMapping("/{requestWorkOrderId}/status")
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

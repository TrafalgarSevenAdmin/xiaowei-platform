package com.xiaowei.worksystem.controller;

import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.ServiceItemDTO;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.query.ServiceItemQuery;
import com.xiaowei.worksystem.service.IServiceItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "工程师添加收费项目")
    @AutoErrorHandler
    @PostMapping("/{workOrderId}/engineer")
    public Result insertByEngineer(@PathVariable("workOrderId") String workOrderId, @RequestBody @Validated(V.Insert.class) List<ServiceItemDTO> serviceItemDTOs, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        List<ServiceItem> serviceItems = FastJsonUtils.objectToList(serviceItemDTOs, ServiceItem.class);
        serviceItems = serviceItemService.saveByEngineer(workOrderId, serviceItems);
        return Result.getSuccess(ObjectToMapUtils.listToMap(serviceItems, fieldsView));
    }

    @ApiOperation(value = "工程师执行服务项目")
    @PutMapping("/{serviceItemId}/execute")
    public Result executeServiceItem(@PathVariable("serviceItemId") String serviceItemId, FieldsView fieldsView) throws Exception {
        ServiceItem serviceItem = serviceItemService.executeServiceItem(serviceItemId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(serviceItem, fieldsView));
    }

    @ApiOperation(value = "质检服务项目")
    @PutMapping("/{serviceItemId}/quality")
    public Result qualityServiceItem(@PathVariable("serviceItemId") String serviceItemId, @RequestBody Boolean audit, FieldsView fieldsView) throws Exception {
        ServiceItem serviceItem = serviceItemService.qualityServiceItem(serviceItemId, audit);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(serviceItem, fieldsView));
    }

    @ApiOperation("服务项目查询接口")
    @GetMapping("")
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

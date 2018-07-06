package com.xiaowei.worksystem.controller;

import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.ServiceItemDTO;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.service.IServiceItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "工程师添加收费项目")
    @AutoErrorHandler
    @PostMapping("/{workOrderId}/engineer")
    public Result insertByEngineer(@PathVariable("workOrderId") String workOrderId, @RequestBody @Validated(V.Insert.class) List<ServiceItemDTO> serviceItemDTOs, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        List<ServiceItem> serviceItems = FastJsonUtils.objectToList(serviceItemDTOs, ServiceItem.class);
        serviceItems = serviceItemService.saveByEngineer(workOrderId,serviceItems);
        List<Map<String, Object>> maps = ObjectToMapUtils.listToMap(serviceItems, fieldsView);
        return Result.getSuccess(maps);
    }


}

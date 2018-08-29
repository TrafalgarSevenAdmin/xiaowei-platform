package com.xiaowei.worksystem.controller;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.EquipmentDTO;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.service.IEquipmentService;
import com.xiaowei.worksystem.service.customer.ICustomerService;
import com.xiaowei.worksystem.service.impl.customer.CustomerServiceImpl;
import com.xiaowei.worksystem.status.CommonStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理
 */
@Api(tags = "设备接口")
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    /**
     * 设备服务
     */
    @Autowired
    private IEquipmentService equipmentService;


    @ApiOperation("根据服务对象id获取其下所有的设备")
    @GetMapping("/{customerId}/equipments")
    @RequiresPermissions("order:customer:info:equipments")
    public Result findBycustomerId(@PathVariable("customerId") String customerId, FieldsView fieldsView) {
        List<Equipment> equipments = equipmentService.findBycustomerId(customerId);
        return Result.getSuccess(ObjectToMapUtils.listToMap(equipments, fieldsView));
    }

    @ApiOperation(value = "添加设备")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:equipment:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) EquipmentDTO equipmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Equipment equipment = BeanCopyUtils.copy(equipmentDTO, Equipment.class);
        equipment = equipmentService.saveEquipment(equipment);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation(value = "修改设备")
    @AutoErrorHandler
    @PutMapping("/{equipmentId}")
    @RequiresPermissions("order:equipment:update")
    public Result update(@RequestBody @Validated(V.Insert.class) EquipmentDTO equipmentDTO, BindingResult bindingResult,
                         @PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) throws Exception {
        Equipment equipment = BeanCopyUtils.copy(equipmentDTO, Equipment.class);
        equipment = equipmentService.update(equipmentId,equipment);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }


    @ApiOperation("根据id获取设备")
    @GetMapping("/{equipmentId}")
    @RequiresPermissions("order:equipment:get")
    public Result findById(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        Equipment equipment = equipmentService.findById(equipmentId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{equipmentId}")
    @RequiresPermissions("order:equipment:delete")
    public Result delete(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        equipmentService.delete(equipmentId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("设备查询接口")
    @GetMapping("")
    @RequiresPermissions("order:equipment:query")
    public Result query(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("status", Filter.Operator.neq,CommonStatus.DELETE.getStatus()));
        if (query.isNoPage()) {
            List<Equipment> equipments = equipmentService.query(query, Equipment.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(equipments, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = equipmentService.queryPage(query, Equipment.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

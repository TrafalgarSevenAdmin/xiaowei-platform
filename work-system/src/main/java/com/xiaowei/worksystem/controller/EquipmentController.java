package com.xiaowei.worksystem.controller;

import com.xiaowei.core.bean.BeanCopyUtils;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "添加设备")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) EquipmentDTO equipmentDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Equipment equipment = BeanCopyUtils.copy(equipmentDTO, Equipment.class);
        equipment = equipmentService.save(equipment);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation(value = "添加设备")
    @AutoErrorHandler
    @PutMapping("/{equipmentId}")
    public Result update(@RequestBody @Validated(V.Insert.class) EquipmentDTO equipmentDTO, BindingResult bindingResult,
                         @PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) throws Exception {
        Equipment equipment = BeanCopyUtils.copy(equipmentDTO, Equipment.class);
        equipment = equipmentService.update(equipmentId,equipment);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }


    @ApiOperation("根据id获取设备")
    @GetMapping("/{equipmentId}")
    public Result findById(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        Equipment equipment = equipmentService.findById(equipmentId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{equipmentId}")
    public Result delete(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        equipmentService.delete(equipmentId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("设备查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
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

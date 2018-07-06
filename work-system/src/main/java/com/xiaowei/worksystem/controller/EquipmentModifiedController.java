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
import com.xiaowei.worksystem.entity.EquipmentModified;
import com.xiaowei.worksystem.service.IEquipmentModifiedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
@RequestMapping("/api/equipment/modified")
public class EquipmentModifiedController {

    /**
     * 审核设备服务
     */
    @Autowired
    private IEquipmentModifiedService equipmentExamineService;

    @ApiOperation(value = "添加待修改的设备",notes = "此接口由工程师提交修改的信息,因此调用此方法必须要工程师登陆")
    @AutoErrorHandler
    @PostMapping("/{workOrderId}")
//    @RequiresRoles("engineer")
    public Result commitModified(@RequestBody @Validated(V.Update.class) EquipmentDTO equipmentDTO, BindingResult bindingResult,
                                 @PathVariable("workOrderId") String workOrderId,FieldsView fieldsView) throws Exception {
        EquipmentModified equipment = BeanCopyUtils.copy(equipmentDTO, EquipmentModified.class);
        equipmentExamineService.commitModified(workOrderId,equipment);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation(value = "通过此设备修改申请",notes = "此接口由后台管理员审核通过")
    @AutoErrorHandler
    @PutMapping("/pass/{modifiedId}")
    public Result passModified(@PathVariable("modifiedId") String modifiedId,FieldsView fieldsView) throws Exception {
        equipmentExamineService.passModified(modifiedId);
        return Result.getSuccess("设备修改申请已通过");
    }

    @ApiOperation(value = "根据id获取待更新的设备信息")
    @GetMapping("/{equipmentId}")
    public Result findById(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        EquipmentModified equipment = equipmentExamineService.findById(equipmentId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(equipment, fieldsView));
    }

    @ApiOperation("删除申请")
    @DeleteMapping("/{equipmentId}")
    public Result delete(@PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) {
        equipmentExamineService.delete(equipmentId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询待审核的设备信息接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<EquipmentModified> equipments = equipmentExamineService.query(query, Equipment.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(equipments, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = equipmentExamineService.queryPage(query, Equipment.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

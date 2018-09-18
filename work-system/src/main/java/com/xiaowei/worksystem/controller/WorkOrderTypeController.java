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
import com.xiaowei.worksystem.dto.WorkOrderTypeDTO;
import com.xiaowei.worksystem.entity.WorkOrderType;
import com.xiaowei.worksystem.query.WorkOrderTypeQuery;
import com.xiaowei.worksystem.service.IWorkOrderTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单服务类型管理
 */
@Api(tags = "工单服务类型接口")
@RestController
@RequestMapping("/api/workOrderType")
public class WorkOrderTypeController {

    @Autowired
    private IWorkOrderTypeService workOrderTypeService;

    @ApiOperation(value = "添加工单服务类型")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加工单服务类型", contentParams = {@ContentParam(useParamField = true, field = "workOrderTypeDTO", value = "工单服务类型信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) WorkOrderTypeDTO workOrderTypeDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        WorkOrderType workOrderType = BeanCopyUtils.copy(workOrderTypeDTO, WorkOrderType.class);
        workOrderType = workOrderTypeService.save(workOrderType);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrderType, fieldsView));
    }

    @ApiOperation(value = "修改工单服务类型")
    @AutoErrorHandler
    @PutMapping("/{WorkOrderTypeId}")
    @HandleLog(type = "修改工单服务类型", contentParams = {@ContentParam(useParamField = true, field = "workOrderTypeDTO", value = "工单服务类型信息"),
            @ContentParam(useParamField = false, field = "WorkOrderTypeId", value = "工单服务类型id")})
    public Result update(@PathVariable("WorkOrderTypeId") String WorkOrderTypeId, @RequestBody @Validated(V.Update.class) WorkOrderTypeDTO workOrderTypeDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        WorkOrderType workOrderType = BeanCopyUtils.copy(workOrderTypeDTO, WorkOrderType.class);
        workOrderType.setId(WorkOrderTypeId);
        workOrderType = workOrderTypeService.update(workOrderType);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrderType, fieldsView));
    }

    @ApiOperation("工单服务类型查询接口")
    @GetMapping("")
    public Result query(WorkOrderTypeQuery workOrderTypeQuery, FieldsView fieldsView) {
        //查询工单服务类型设置默认条件
        setDefaultCondition(workOrderTypeQuery);

        if (workOrderTypeQuery.isNoPage()) {
            List<WorkOrderType> workOrderTypes = workOrderTypeService.query(workOrderTypeQuery, WorkOrderType.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(workOrderTypes, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = workOrderTypeService.queryPage(workOrderTypeQuery, WorkOrderType.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(WorkOrderTypeQuery workOrderTypeQuery) {

    }

    @ApiOperation("根据id获取工单服务类型")
    @GetMapping("/{WorkOrderTypeId}")
    public Result findById(@PathVariable("WorkOrderTypeId") String WorkOrderTypeId, FieldsView fieldsView) {
        WorkOrderType workOrderType = workOrderTypeService.findById(WorkOrderTypeId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workOrderType, fieldsView));
    }

    @ApiOperation("删除工单服务类型")
    @DeleteMapping("/{WorkOrderTypeId}")
    @HandleLog(type = "删除工单服务类型", contentParams = {@ContentParam(field = "WorkOrderTypeId", value = "工单服务类型id")})
    public Result delete(@PathVariable("WorkOrderTypeId") String WorkOrderTypeId, FieldsView fieldsView) {
        workOrderTypeService.delete(WorkOrderTypeId);
        return Result.getSuccess();
    }


}

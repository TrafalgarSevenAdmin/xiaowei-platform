package com.xiaowei.worksystem.controller.flow;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.WorkFlowDTO;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import com.xiaowei.worksystem.query.WorkFlowQuery;
import com.xiaowei.worksystem.service.IWorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "流程模板接口")
@RestController
@RequestMapping("/api/workFlow")
public class WorkFlowController {

    @Autowired
    private IWorkFlowService workFlowService;

    @ApiOperation(value = "添加流程模板")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) WorkFlowDTO workFlowDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        WorkFlow workFlow = BeanCopyUtils.copy(workFlowDTO, WorkFlow.class);
        workFlow = workFlowService.saveWorkFlow(workFlow);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workFlow, fieldsView));
    }

    @ApiOperation(value = "修改流程模板")
    @AutoErrorHandler
    @PutMapping("/{workFlowId}")
    public Result update(@PathVariable("workFlowId") String workFlowId, @RequestBody @Validated(V.Update.class) WorkFlowDTO workFlowDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        WorkFlow workFlow = BeanCopyUtils.copy(workFlowDTO, WorkFlow.class);
        workFlow.setId(workFlowId);
        workFlow = workFlowService.updateWorkFlow(workFlow);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workFlow, fieldsView));
    }

    @ApiOperation("删除流程模板")
    @DeleteMapping("/{workFlowId}")
    public Result delete(@PathVariable("workFlowId") String workFlowId, FieldsView fieldsView) {
        workFlowService.deleteWorkFlow(workFlowId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("流程模板查询接口")
    @GetMapping("")
    public Result query(WorkFlowQuery workFlowQuery, FieldsView fieldsView) {
        //流程模板查询接口设置默认条件
        setDefaultCondition(workFlowQuery);
        if (workFlowQuery.isNoPage()) {
            List<WorkFlow> workFlows = workFlowService.query(workFlowQuery,WorkFlow.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(workFlows, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = workFlowService.queryPage(workFlowQuery,WorkFlow.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(WorkFlowQuery workFlowQuery) {

    }

    @ApiOperation("根据id获取流程模板")
    @GetMapping("/{workFlowId}")
    public Result findById(@PathVariable("workFlowId") String workFlowId, FieldsView fieldsView) {
        WorkFlow workFlow = workFlowService.findById(workFlowId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workFlow, fieldsView));
    }
}

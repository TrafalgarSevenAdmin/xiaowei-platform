package com.xiaowei.flow.controller;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.flow.constants.DataFieldsConst;
import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.service.IFlowDefinitionService;
import com.xiaowei.flow.service.IFlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@Api(tags = "流程定义")
@RestController
@RequestMapping("/api/flow/definition")
public class FlowDefinitionController {

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private IFlowNodeService flowNodeService;

    @AutoErrorHandler
    @PostMapping("")
    @ApiOperation("添加流程")
    public Result insert(@RequestBody @Validated(V.Insert.class) FlowDefinition flowDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowDefinition flow = BeanCopyUtils.copy(flowDto, FlowDefinition.class);
        flow = flowDefinitionService.save(flow);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(flow, fieldsView));
    }

    @ApiOperation("获取流程定义")
    @GetMapping("/{flowId}")
    public Result findById(@PathVariable("flowId") String flowId, FieldsView fieldsView) {
        FlowDefinition flow = flowDefinitionService.findById(flowId);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(flow, fieldsView));
    }

    @ApiOperation("获取流程的所有节点")
    @GetMapping("/{flowId}/nodes")
    public Result findAllNode(@PathVariable("flowId") String flowId, FieldsView fieldsView ) {
        if (!fieldsView.isInclude()) {
            //过滤掉不需要再此返回给前端的字段
            fieldsView.getFields().addAll(DataFieldsConst.nodeViewFilters);
        }
        List<FlowNode> flowNodes = flowNodeService.findAllNodes(flowId);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(flowNodes, fieldsView));
    }

    @ApiOperation("删除流程定义")
    @DeleteMapping("/{flowId}")
    public Result delete(@PathVariable("flowId") String flowId, FieldsView fieldsView) {
        flowDefinitionService.delete(flowId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("流程定义查询")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {

        if (query.isNoPage()) {
            List<FlowDefinition> flows = flowDefinitionService.query(query, FlowDefinition.class);
            return Result.getSuccess(ObjectToMapUtils.anyToHandleField(flows, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = flowDefinitionService.queryPage(query, FlowDefinition.class);
            pageResult.setRows(ObjectToMapUtils.anyToHandleField(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

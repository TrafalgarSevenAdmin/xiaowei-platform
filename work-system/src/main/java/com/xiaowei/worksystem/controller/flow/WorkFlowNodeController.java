package com.xiaowei.worksystem.controller.flow;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.WorkFlowNodeDTO;
import com.xiaowei.worksystem.entity.flow.WorkFlowNode;
import com.xiaowei.worksystem.query.WorkFlowNodeQuery;
import com.xiaowei.worksystem.service.IWorkFlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "节点池接口")
@RestController
@RequestMapping("/api/flowNode")
public class WorkFlowNodeController {
    @Autowired
    private IWorkFlowNodeService workFlowNodeService;

    @ApiOperation(value = "添加流程节点")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:flownode:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) WorkFlowNodeDTO workFlowNodeDTO, BindingResult bindingResult) throws Exception {
        WorkFlowNode workFlowNode = BeanCopyUtils.copy(workFlowNodeDTO, WorkFlowNode.class);
        return Result.getSuccess(workFlowNodeService.save(workFlowNode));
    }

    @ApiOperation(value = "修改流程节点")
    @AutoErrorHandler
    @PutMapping("/{workFlowNodeId}")
    @RequiresPermissions("order:flownode:update")
    public Result update(@PathVariable("workFlowNodeId") String workFlowNodeId, @RequestBody @Validated(V.Update.class) WorkFlowNodeDTO workFlowNodeDTO, BindingResult bindingResult) throws Exception {
        WorkFlowNode workFlowNode = BeanCopyUtils.copy(workFlowNodeDTO, WorkFlowNode.class);
        workFlowNode.setId(workFlowNodeId);
        workFlowNode = workFlowNodeService.updateWorkFlowNode(workFlowNode);
        return Result.getSuccess(workFlowNode);
    }

    @ApiOperation("删除流程节点")
    @DeleteMapping("/{workFlowNodeId}")
    @RequiresPermissions("order:flownode:delete")
    public Result delete(@PathVariable("workFlowNodeId") String workFlowNodeId) {
        workFlowNodeService.delete(workFlowNodeId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("流程节点查询接口")
    @GetMapping("")
    @RequiresPermissions("order:flownode:query")
    public Result query(WorkFlowNodeQuery workFlowNodeQuery, FieldsView fieldsView) {
        //流程节点查询接口设置默认条件
        setDefaultCondition(workFlowNodeQuery);
        if (workFlowNodeQuery.isNoPage()) {
            List<WorkFlowNode> workFlowNodes = workFlowNodeService.query(workFlowNodeQuery,WorkFlowNode.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(workFlowNodes, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = workFlowNodeService.queryPage(workFlowNodeQuery,WorkFlowNode.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(WorkFlowNodeQuery workFlowNodeQuery) {

    }

    @ApiOperation("根据id获取权限")
    @GetMapping("/{workFlowNodeId}")
    @RequiresPermissions("order:flownode:get")
    public Result findById(@PathVariable("workFlowNodeId") String workFlowNodeId, FieldsView fieldsView) {
        WorkFlowNode workFlowNode = workFlowNodeService.findById(workFlowNodeId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workFlowNode, fieldsView));
    }
}

package com.xiaowei.flow.controller;

import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.flow.constants.DataFieldsConst;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.manager.FlowManager;
import com.xiaowei.flow.service.IFlowTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Log4j2
@Api(tags = "流程任务操作")
@RestController
@RequestMapping("/api/flow/task")
public class FlowTaskController {

    @Autowired
    private IFlowTaskService flowTaskService;

    @Autowired
    private FlowManager flowManager;

    @ApiOperation("获取流程任务")
    @GetMapping("/{flowTaskId}")
    public Result findById(@PathVariable("flowTaskId") String flowTaskId, FieldsView fieldsView) {
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        FlowTask flowTask = flowTaskService.findById(flowTaskId);
        return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(flowTask, fieldsView));
    }

    @ApiOperation("获取代办任务")
    @GetMapping("/me/todo")
    public Result todoTask(Query query,FieldsView fieldsView) {
        return Result.getSuccess(flowManager.getTodoTaskManager().findMyTodoTask(query,fieldsView));
    }

    @ApiOperation("获取抄送任务")
    @GetMapping("/me/view")
    public Result viewTask(Query query,FieldsView fieldsView) {
        return Result.getSuccess( flowManager.getViewTaskManager().findMyViewTask(query,fieldsView));
    }

    @ApiOperation("获取已处理任务")
    @GetMapping("/me/completed")
    public Result completedTask(Query query,FieldsView fieldsView) {
        return Result.getSuccess(flowManager.getMeCompleteTaskManager().findMyCompleteTask(query,fieldsView));
    }

    @ApiOperation("查询流程任务")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        if (query.isNoPage()) {
            List<FlowTask> flowTasks = flowTaskService.query(query, FlowTask.class);
            return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(flowTasks, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = flowTaskService.queryPage(query, FlowTask.class);
            pageResult.setRows(ObjectToMapUtils.AnyToHandleField(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

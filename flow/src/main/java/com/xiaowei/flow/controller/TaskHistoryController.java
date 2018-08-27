package com.xiaowei.flow.controller;

import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.flow.constants.DataFieldsConst;
import com.xiaowei.flow.entity.FlowTaskExecuteHistory;
import com.xiaowei.flow.service.ITaskHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@Api(tags = "任务操作记录")
@RestController
@RequestMapping("/api/flow/history")
public class TaskHistoryController {

    @Autowired
    private ITaskHistoryService taskHistoryService;

    @ApiOperation("获取任务记录")
    @GetMapping("/{taskHistoryId}")
    public Result findById(@PathVariable("taskHistoryId") String taskHistoryId, FieldsView fieldsView) {
        //移除不需要的字段,避免循环，减少流量
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskExecuteHistoryViewFilters);
        }
        FlowTaskExecuteHistory flowTaskExecuteHistory = taskHistoryService.findById(taskHistoryId);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(flowTaskExecuteHistory, fieldsView));
    }

    @ApiOperation("查询任务记录")
    @GetMapping("")
    public Result query(Query query,FieldsView fieldsView) {

        //移除不需要的字段,避免循环，减少流量
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskExecuteHistoryViewFilters);
        }
        if (query.isNoPage()) {
            List<FlowTaskExecuteHistory> flowTaskExecuteHistories = taskHistoryService.query(query, FlowTaskExecuteHistory.class);
            Object data = ObjectToMapUtils.anyToHandleField(flowTaskExecuteHistories, fieldsView);
            return Result.getSuccess(data);//以list形式返回,没有层级
        } else {
            PageResult pageResult = taskHistoryService.queryPage(query, FlowTaskExecuteHistory.class);
            pageResult.setRows(ObjectToMapUtils.anyToHandleField(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

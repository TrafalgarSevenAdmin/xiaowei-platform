package com.xiaowei.worksystem.controller.assets;

import com.alibaba.fastjson.JSON;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.flow.constants.DataFieldsConst;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.extend.TaskNodeComplete;
import com.xiaowei.flow.manager.TaskManager;
import com.xiaowei.flow.pojo.CreateTaskParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;
import com.xiaowei.worksystem.dto.AuditingDto;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.entity.assets.Warehouse;
import com.xiaowei.worksystem.service.IInventoryService;
import com.xiaowei.worksystem.service.assets.IInvOrderInService;
import com.xiaowei.worksystem.service.impl.InventoryServiceImpl;
import com.xiaowei.worksystem.status.CommonStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Api(tags = "入库单")
@RestController
@RequestMapping("/api/assets/invOrderIn")
public class InvOrderInController {

    @Autowired
    private IInvOrderInService invOrderInService;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private TaskManager taskManager;

    @ApiOperation(value = "创建入库单申请",notes = "这里不保存到数据库，只保存到流程中")
    @AutoErrorHandler
    @PostMapping("/task")
    public Result insert(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderIn, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask rkd = taskManager.createTask(CreateTaskParameter.builder()
                .flowCode("RKD")
                //这里应该采用统一的单号生成器
                .code(UUID.randomUUID().toString())
                .name("入库单")
                .ext(FastJsonUtils.objectToJson(invOrderIn))
                .build());
        //填写节点完成
        taskManager.completeTask(rkd.getId());
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(rkd, fieldsView));
    }

    @ApiOperation(value = "审核入库单申请")
    @AutoErrorHandler
    @PutMapping("/task")
    public Result complete(@RequestBody @Validated() AuditingDto auditingDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask rkd = taskManager.completeTask(auditingDto.getTaskId(), new TaskNodeComplete() {
            @Override
            public TaskCompleteExtendResult execute(TaskCompleteExtendParameter parameter) {
                //不通过，就跳转到上一个节点，重新填写
                if (!auditingDto.getPass()) {
                    return TaskCompleteExtendResult.builder().reason(auditingDto.getReason())
                            .nextNodeId(parameter.getLastHistory().getNode().getId()).build();
                }
                //通过
                return TaskCompleteExtendResult.builder().reason(auditingDto.getReason()).build();
            }

            @Override
            public void complete(TaskCompleteExtendParameter parameter) {
                //节点完成，不在乎
            }
        },parameter -> {
            //任务完成
            InvOrderIn invOrderIn = JSON.parseObject(parameter.getTask().getExt(), InvOrderIn.class);
            invOrderIn.setAuditReason(parameter.getLastHistory().getReason());
            invOrderIn.setAuditTime(parameter.getLastHistory().getCreatedTime());
            invOrderIn.setAuditUserId(parameter.getLastHistory().getOperationUserId());
            invOrderIn.setAuditUserName(parameter.getLastHistory().getOperationUserName());
            invOrderIn = invOrderInService.save(invOrderIn);
            //todo 变更仓库中的数据
            invOrderIn.getOutWarehouse();
            invOrderIn.getInWarehouse();
        });
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(rkd, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{invOrderInId}")
    public Result update(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderInDto, BindingResult bindingResult,
                         @PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) throws Exception {
        InvOrderIn invOrderIn = BeanCopyUtils.copy(invOrderInDto, InvOrderIn.class);
        invOrderIn = invOrderInService.save(invOrderIn);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{invOrderInId}")
    public Result findById(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        InvOrderIn invOrderIn = invOrderInService.findById(invOrderInId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{invOrderInId}")
    public Result delete(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        invOrderInService.delete(invOrderInId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("status", Filter.Operator.neq, CommonStatus.DELETE.getStatus()));
        if (query.isNoPage()) {
            List<InvOrderIn> invOrderIns = invOrderInService.query(query, InvOrderIn.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(invOrderIns, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = invOrderInService.queryPage(query, InvOrderIn.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

package com.xiaowei.worksystem.controller.assets;

import com.alibaba.fastjson.JSON;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.exception.BusinessException;
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
import com.xiaowei.flow.manager.FlowManager;
import com.xiaowei.flow.manager.TaskManager;
import com.xiaowei.flow.pojo.CreateTaskParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;
import com.xiaowei.worksystem.dto.AuditingDto;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.entity.assets.InvOrderOut;
import com.xiaowei.worksystem.service.assets.IInvOrderOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Api(tags = "出库单")
@Slf4j
@RestController
@RequestMapping("/api/assets/invOrderOut")
public class InvOrderOutController {

    @Autowired
    private IInvOrderOutService invOrderOutService;

    @Autowired
    FlowManager flowManager;

    @ApiOperation(value = "创建出单申请",notes = "这里不保存到数据库，只保存到流程中")
    @AutoErrorHandler
    @PostMapping("/task")
    public Result insert(@RequestBody @Validated(V.Insert.class) InvOrderOut invOrderOut, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask ckd;
        if (StringUtils.isNotBlank(invOrderOut.getCode())) {
            ckd = flowManager.getTaskManager().findTaskByCode(invOrderOut.getCode());
            //判断这个任务是否在填写节点
            if (!ckd.getNextNode().getCode().equals("start")) {
                throw new BusinessException("任务不在填写节点！");
            }
            //完成填写节点
            flowManager.getTaskManager().completeTask(ckd.getId(), new TaskNodeComplete() {
                @Override
                public TaskCompleteExtendResult execute(TaskCompleteExtendParameter parameter) {
                    parameter.getTask().setExt(FastJsonUtils.objectToJson(invOrderOut));
                    return TaskCompleteExtendResult.builder().build();
                }

                @Override
                public void complete(TaskCompleteExtendParameter parameter) {

                }
            });
        }
        ckd = flowManager.getTaskManager().createTask(CreateTaskParameter.builder()
                .flowCode("CK")
                //这里应该采用统一的单号生成器
                .code(UUID.randomUUID().toString())
                .name("出库单")
                .ext(FastJsonUtils.objectToJson(invOrderOut))
                .build());
        //填写节点完成
        ckd = flowManager.getTaskManager().completeTask(ckd.getId());
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(ckd, fieldsView));
    }

    @ApiOperation(value = "审核出库单申请")
    @AutoErrorHandler
    @PutMapping("/task")
    public Result complete(@RequestBody @Validated() AuditingDto auditingDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask ckd = flowManager.getTaskManager().completeTask(auditingDto.getTaskId(), new TaskNodeComplete() {
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
            InvOrderOut invOrderOut = JSON.parseObject(parameter.getTask().getExt(),InvOrderOut.class);
            invOrderOut.setAuditReason(parameter.getLastHistory().getReason());
            invOrderOut.setAuditTime(parameter.getLastHistory().getCreatedTime());
            invOrderOut.setAuditUserId(parameter.getLastHistory().getOperationUserId());
            invOrderOut.setAuditUserName(parameter.getLastHistory().getOperationUserName());
            invOrderOut.setCode(parameter.getTask().getCode());
            invOrderOut = invOrderOutService.save(invOrderOut);
            //todo 变更仓库中的数据
            invOrderOut.getOutWarehouse();
            invOrderOut.getInWarehouse();
        });
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.AnyToHandleField(ckd, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{invOrderOutId}")
    public Result update(@RequestBody @Validated(V.Insert.class) InvOrderOut invOrderOutDto, BindingResult bindingResult,
                         @PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) throws Exception {
        InvOrderOut invOrderOut = BeanCopyUtils.copy(invOrderOutDto, InvOrderOut.class);
        invOrderOut = invOrderOutService.save(invOrderOut);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderOut, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{invOrderOutId}")
    public Result findById(@PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) {
        InvOrderOut invOrderOut = invOrderOutService.findById(invOrderOutId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderOut, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{invOrderOutId}")
    public Result delete(@PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) {
        invOrderOutService.delete(invOrderOutId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<InvOrderOut> invOrderOuts = invOrderOutService.query(query, InvOrderOut.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(invOrderOuts, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = invOrderOutService.queryPage(query, InvOrderOut.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}

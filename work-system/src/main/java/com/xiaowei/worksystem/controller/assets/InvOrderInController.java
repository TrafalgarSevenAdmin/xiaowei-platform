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
import com.xiaowei.flow.pojo.CreateTaskParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;
import com.xiaowei.worksystem.dto.AuditingDto;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.service.assets.IInvOrderInService;
import com.xiaowei.worksystem.status.CommonStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@Api(tags = "入库单")
@Slf4j
@RestController
@RequestMapping("/api/assets/invOrderIn")
public class InvOrderInController {

    @Autowired
    private IInvOrderInService invOrderInService;

    @Autowired
    private FlowManager flowManager;

    @ApiOperation(value = "创建入库单申请",notes = "这里不保存到数据库，只保存到流程中")
    @AutoErrorHandler
    @PostMapping("/task")
    @RequiresPermissions("order:assets:inventory:in:create")
    public Result insert(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderIn, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask rkd;
        if (StringUtils.isNotBlank(invOrderIn.getCode())) {
            rkd = flowManager.getTaskManager().findTaskByCode(invOrderIn.getCode());
            //判断这个任务是否在填写节点
            if (!rkd.getNextNode().getCode().equals("start")) {
                throw new BusinessException("任务不在填写节点！");
            }
            //完成填写节点
            flowManager.getTaskManager().completeTask(rkd.getId(), new TaskNodeComplete() {
                @Override
                public TaskCompleteExtendResult execute(TaskCompleteExtendParameter parameter) {
                    parameter.getTask().setExt(FastJsonUtils.objectToJson(invOrderIn));
                    return TaskCompleteExtendResult.builder().build();
                }

                @Override
                public void complete(TaskCompleteExtendParameter parameter) {

                }
            });
        } else {
            rkd = flowManager.getTaskManager().createTask(CreateTaskParameter.builder()
                    .flowCode("RK")
                    //这里应该采用统一的单号生成器
                    .code(UUID.randomUUID().toString())
                    .name("入库单")
                    .ext(FastJsonUtils.objectToJson(invOrderIn))
                    .build());
            //填写节点完成
            flowManager.getTaskManager().completeTask(rkd.getId());
        }

        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(rkd, fieldsView));
    }

    @ApiOperation(value = "审核入库单申请")
    @AutoErrorHandler
    @PutMapping("/task")
    @RequiresPermissions("order:assets:inventory:in:auditing")
    public Result complete(@RequestBody @Validated() AuditingDto auditingDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        FlowTask rkd = flowManager.getTaskManager().completeTask(auditingDto.getTaskId(), new TaskNodeComplete() {
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
                log.debug("入库单任务{{}}的节点{}由{}完成", parameter.getTask().getCode(), parameter.getLastHistory().getNode().getName(), parameter.getLastHistory().getOperationUserName());
            }
        },parameter -> {
            //任务完成
            InvOrderIn invOrderIn = JSON.parseObject(parameter.getTask().getExt(), InvOrderIn.class);
            invOrderIn.setAuditReason(parameter.getLastHistory().getReason());
            invOrderIn.setAuditTime(parameter.getLastHistory().getCreatedTime());
            invOrderIn.setAuditUserId(parameter.getLastHistory().getOperationUserId());
            invOrderIn.setAuditUserName(parameter.getLastHistory().getOperationUserName());
            invOrderIn.setCode(parameter.getTask().getCode());
            invOrderIn = invOrderInService.save(invOrderIn);
            log.debug("开始变更仓库中的数据");
            invOrderInService.handleInvOrderIn(invOrderIn);
        });
        if (!fieldsView.isInclude()) {
            fieldsView.getFields().addAll(DataFieldsConst.taskViewFilters);
        }
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(rkd, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{invOrderInId}")
    @RequiresPermissions("order:assets:inventory:in:update")
    public Result update(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderInDto, BindingResult bindingResult,
                         @PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) throws Exception {
        InvOrderIn invOrderIn = BeanCopyUtils.copy(invOrderInDto, InvOrderIn.class);
        invOrderIn = invOrderInService.save(invOrderIn);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{invOrderInId}")
    @RequiresPermissions("order:assets:inventory:in:get")
    public Result findById(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        InvOrderIn invOrderIn = invOrderInService.findById(invOrderInId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{invOrderInId}")
    @RequiresPermissions("order:assets:inventory:in:delete")
    public Result delete(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        invOrderInService.delete(invOrderInId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    @RequiresPermissions("order:assets:inventory:in:query")
    public Result query(Query query, FieldsView fieldsView) {
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

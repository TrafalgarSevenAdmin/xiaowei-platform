package com.xiaowei.flow.manager;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.flow.constants.TaskActionType;
import com.xiaowei.flow.constants.TaskNotifyPersonType;
import com.xiaowei.flow.constants.TaskStatus;
import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.entity.FlowTaskExecuteHistory;
import com.xiaowei.flow.entity.auth.AuthGrant;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.extend.TaskComplete;
import com.xiaowei.flow.extend.TaskNodeComplete;
import com.xiaowei.flow.extend.TaskNotifyExtend;
import com.xiaowei.flow.pojo.CreateTaskParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;
import com.xiaowei.flow.pojo.TaskNotifyContext;
import com.xiaowei.flow.service.ITaskHistoryService;
import com.xiaowei.flow.service.IFlowNodeService;
import com.xiaowei.flow.service.IFlowDefinitionService;
import com.xiaowei.flow.service.IFlowTaskService;
import com.xiaowei.flow.service.auth.IAuthGrantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务管理
 * 供程序调用使用
 */
@Slf4j
@Component
public class TaskManager {

    @Autowired
    LoginUser loginUser;

    @Autowired
    TaskNodeComplete taskNodeComplete;

    @Autowired
    TaskComplete taskComplete;

    @Autowired
    TaskNotifyExtend taskNotifyExtend;

    @Autowired
    IFlowTaskService flowTaskService;

    @Autowired
    IFlowDefinitionService flowDefinitionService;

    @Autowired
    IFlowNodeService flowNodeService;

    @Autowired
    IAuthGrantService authGrantService;

    @Autowired
    ITaskHistoryService taskHistoryService;

    /**
     * 创建一个任务。
     * @param parameter
     */
    public FlowTask createTask(CreateTaskParameter parameter) {
        Assert.isTrue(StringUtils.isNotBlank(parameter.getFlowCode()), "流程代码不能为空");
        FlowDefinition flow = flowDefinitionService.findByCode(parameter.getFlowCode());
        Assert.notNull(flow, "未找到流程:"+parameter.getFlowCode());
        FlowTask flowTask = FlowTask.builder()
                .name(parameter.getName())
                .code(parameter.getCode())
                .createUserId(loginUser.getUserId())
                .createUserName(loginUser.getUserName())
                .ext(parameter.getExt())
                .status(TaskStatus.Created)
                .flow(flow)
                .nextNode(flow.getStart())
                .updateTime(new Date())
                .build();
        //保存任务
        flowTask = flowTaskService.save(flowTask);
        String taskId = flowTask.getId();
        log.debug("正在创建新的任务:{}，任务id:{}", parameter.getName(),parameter.getCode());

        // TODO: 2018/8/22 0022 任务通知
        log.debug("配置抄送人");
        if (null != parameter.getViewer()) {
            flowTask.setViewer(parameter.getViewer());
        } else {
            log.debug("没有配置抄送人，取默认的抄送人中。。。");
            List<AuthGrant> authViewers = new ArrayList<>();
            for (AuthGrant auth : flow.getViewer()) {
                authViewers.add(this.createGrant(auth, taskId, null, null));
            }
            flowTask.setViewer(authViewers);
        }

        log.debug("配置审核人");
        List<AuthGrant> configAuthGrants = new ArrayList<>();
        Map<String, List<AuthGrant>> auditors = parameter.getAuditors();
        if (auditors == null) {
            auditors = new HashMap<>();
        }
        List<FlowNode> allNodes = flowNodeService.findAllNodes(flow.getId());
        for (FlowNode node : allNodes) {
            List<AuthGrant> authGrants = auditors.get(node.getId());
            //若在此任务中此节点未配置审核人，那么取默认的审核人
            if (CollectionUtils.isEmpty(authGrants)) {
                log.debug("没有配置审核人，取默认的审核人中。。。");
                for (AuthGrant defaultAuthGrant : node.getAuth()) {
                    configAuthGrants.add(this.createGrant(defaultAuthGrant, taskId, null, node.getId()));
                }
            } else {
                //有就取此节点配置的审核人
                for (AuthGrant configAuthGrant : authGrants) {
                    configAuthGrants.add(this.createGrant(configAuthGrant, taskId, null, node.getId()));
                }
            }
        }
        authGrantService.save(configAuthGrants);
        //再次保存任务
        flowTaskService.save(flowTask);
        //通知节点消息
        TaskHandlerNotify(flowTask);
        return flowTask;
    }

    /**
     * 关闭任务
     * @param taskId    任务id
     * @param reason    关闭原因
     * @return
     */
    public FlowTask closeTask(String taskId,String reason) {
        log.debug("关闭任务{}中", taskId);
        FlowTask task = flowTaskService.findById(taskId);
        Assert.notNull(task, "没有找到该任务！");
        Assert.isTrue(task.getStatus()!=TaskStatus.Finished, "当前任务已经完成！");
        Assert.isTrue(task.getStatus() != TaskStatus.Close, "当前任务已经被关闭");
        //创建一个执行历史
        //记录执行任务的数据
        FlowTaskExecuteHistory nowTaskHistory = FlowTaskExecuteHistory.builder()
                .lastTask(task.getNowTaskHistory())
                //关闭记录无当前节点，纯粹为了记录操作
                .node(null)
                .action(TaskActionType.CLOSE)
                .reason(reason)
                .operationUserId(loginUser.getUserId())
                .operationUserName(loginUser.getUserName())
                .task(task)
                .build();
        nowTaskHistory.setCreatedTime(new Date());
        taskHistoryService.save(nowTaskHistory);
        log.debug("保存关闭任务{}的操作记录",taskId);
        task.setStatus(TaskStatus.Close);
        task.setNowTaskHistory(nowTaskHistory);
        task.setNextNode(null);
        task = flowTaskService.save(task);
        return task;
    }

    /**
     * 执行任务
     * @param taskId     任务id
     * @return
     */
    @Transactional
    public FlowTask completeTask(@NotNull String taskId) {
        return this.completeTask(taskId, null);
    }

    @Transactional
    public FlowTask completeTask(@NotNull String taskId,TaskNodeComplete taskNodeComplete) {
        return this.completeTask(taskId, taskNodeComplete,null);
    }

    /**
     * 执行任务
     * 应当控制多次点击提交的操作，否者会将任务多次提交，造成严重错误
     * 建议在complete回调中判断前端上传的historyId是否与现目前流程的上一个历史记录一致
     * @param taskId     任务id
     * @param taskNodeComplete 任务节点处理回调
     * @param taskComplete 任务完成回调
     * @return
     */
    @Transactional
    public FlowTask completeTask(@NotNull String taskId, TaskNodeComplete taskNodeComplete,TaskComplete taskComplete) {
        log.debug("执行任务{}中", taskId);
        FlowTask task = flowTaskService.findById(taskId);
        Assert.notNull(task, "没有找到该任务！");
        Assert.isTrue(task.getStatus()!=TaskStatus.Finished, "当前任务已经完成！");
        Assert.isTrue(task.getStatus()!=TaskStatus.Close, "当前任务已经被关闭");
        FlowNode nowNode = task.getNextNode();
        Assert.notNull(nowNode, "任务运行状态错误!当前任务状态："+task.getStatus());
        task.setStatus(TaskStatus.Running);
        //懒得查。查询此任务此节点的权限是否能够调用

        //执行任务处理
        if (taskNodeComplete == null) {
            //若没有指定任务处理器，那么就使用默认的任务处理器
            //此任务处理器应该实现一些注解操作等
            taskNodeComplete = this.taskNodeComplete;
        }
        log.debug("任务执行业务回调执行中...");
        TaskCompleteExtendResult result = taskNodeComplete.execute(TaskCompleteExtendParameter.builder()
                .task(task)
                .flowDefinition(task.getFlow())
                .lastHistory(task.getNowTaskHistory())
                .build()
        );
        //业务指定的下一个节点
        FlowNode tempNextNode = null;
        if (StringUtils.isNotBlank(result.getNextNodeId())) {
            tempNextNode = flowNodeService.findById(result.getNextNodeId());
            Assert.notNull(tempNextNode, "未找到下一节点");
        }

        //获取配置指定的下一个节点
        FlowNode configNextNode = null;
        Collection<FlowNode> nextNodes = nowNode.getNextNodes();
        if (CollectionUtils.isNotEmpty(nextNodes)) {
            // TODO: 2018/8/21 0021 此为第一期，暂时不做分支节点功能
            //直接取第一个节点
            configNextNode = nextNodes.iterator().next();
        }

        FlowNode nextNode = tempNextNode != null ? tempNextNode : configNextNode;

        log.debug("任务执行记录创建中...");
        //记录执行任务的数据
        FlowTaskExecuteHistory nowTaskHistory = FlowTaskExecuteHistory.builder()
                .lastTask(task.getNowTaskHistory())
                .node(nowNode)
                .ext(result.getExt())
                .reason(result.getReason())
                .operationUserId(loginUser.getUserId())
                .operationUserName(loginUser.getUserName())
                .task(task)
                .build();
        nowTaskHistory.setCreatedTime(new Date());

        //判断动作
        if (tempNextNode == null || configNextNode==null || tempNextNode.getId().equals(configNextNode.getId())) {
            //没有指定下一个任务或到达完成节点或者下一个任务与流程定义一致，说明任务运行正常
            nowTaskHistory.setAction(TaskActionType.NORMAL);
        }else {
            //任务未按照配置流程顺序运行
            nowTaskHistory.setAction(TaskActionType.ABNORMAL);
        }

        if (nextNode == null) {
            //找不到最后的节点就说明任务完成
            log.debug("任务{}完成", task.getId());
            //任务完成
            task.setStatus(TaskStatus.Finished);
            //当前动作属于完成
            nowTaskHistory.setAction(TaskActionType.Finished);
        }

        //保存操作记录
        log.debug("保存任务执行记录");
        taskHistoryService.save(nowTaskHistory);

        task.setNextNode(nextNode);
        task.setUpdateTime(new Date());
        task.setNowTaskHistory(nowTaskHistory);

        log.debug("任务节点{{}}执行完成",nowNode.getName());
        task = flowTaskService.save(task);

        //节点完成回调
        taskNodeComplete.complete(TaskCompleteExtendParameter.builder()
                .task(task)
                .flowDefinition(task.getFlow())
                .lastHistory(task.getNowTaskHistory())
                .build());
        //通知节点消息
        TaskHandlerNotify(task);

        //任务完成业务回调
        if (nextNode == null) {
            //执行任务处理
            if (taskComplete == null) {
                //若没有指定任务处理器，那么就使用默认的任务处理器
                taskComplete = this.taskComplete;
            }
            //任务完成通知回调
            taskComplete.complete(TaskCompleteExtendParameter.builder()
                    .task(task)
                    .flowDefinition(task.getFlow())
                    .lastHistory(task.getNowTaskHistory())
                    .build());
        }
        return task;
    }

    private void TaskHandlerNotify(FlowTask task) {

        //抄送人
        List<AuthGrant> viewers = task.getViewer();
        taskNotifyExtend.notifyAction(TaskNotifyContext.builder()
                .task(task)
                .userIds(viewers.stream().filter(v->v.getUserId()!=null).map(AuthGrant::getUserId).collect(Collectors.toSet()))
                .departmentIds(viewers.stream().filter(v->v.getDepartmentId()!=null).map(AuthGrant::getDepartmentId).collect(Collectors.toSet()))
                .roleIds(viewers.stream().filter(v->v.getRoleId()!=null).map(AuthGrant::getRoleId).collect(Collectors.toSet()))
                .notifyPersonType(TaskNotifyPersonType.Viewer)
                .build());

        if (task.getNextNode() == null) {
            return;
        }
        //任务处理人
        List<AuthGrant> auditorAuths = authGrantService.query(new Query().addFilter(Filter.eq("taskId", task.getId())).addFilter(Filter.eq("nodeId", task.getNextNode().getId())));
        taskNotifyExtend.notifyAction(TaskNotifyContext.builder()
                .task(task)
                .userIds(auditorAuths.stream().filter(v->v.getUserId()!=null).map(AuthGrant::getUserId).collect(Collectors.toSet()))
                .departmentIds(auditorAuths.stream().filter(v->v.getDepartmentId()!=null).map(AuthGrant::getDepartmentId).collect(Collectors.toSet()))
                .roleIds(auditorAuths.stream().filter(v->v.getRoleId()!=null).map(AuthGrant::getRoleId).collect(Collectors.toSet()))
                .notifyPersonType(TaskNotifyPersonType.Auditor)
                .build());
    }
    private AuthGrant createGrant(AuthGrant authGrant, String taskId, String flowId,String nodeId) {
        return AuthGrant.builder()
                .taskId(taskId)
                .flowId(flowId)
                .nodeId(nodeId)
                .userId(authGrant.getUserId())
                .departmentId(authGrant.getDepartmentId())
                .roleId(authGrant.getRoleId())
                .build();
    }

}

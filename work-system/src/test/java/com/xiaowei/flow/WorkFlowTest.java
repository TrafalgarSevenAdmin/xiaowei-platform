package com.xiaowei.flow;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.entity.auth.AuthGrant;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.manager.TaskManager;
import com.xiaowei.flow.manager.FlowManager;
import com.xiaowei.flow.pojo.CreateTaskParameter;
import com.xiaowei.flow.service.IFlowDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;


@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = WorkSystemApplication.class)
public class WorkFlowTest {

    @Autowired
    IFlowDefinitionService flowDefinitionService;

    @Autowired
    TaskManager taskManager;

    @Autowired
    FlowManager flowManager;

    @Autowired
    LoginUser loginUser;

    /**
     * 创建任务并跑部分节点
     */
    @Test
    public void createTask() {
        FlowTask task = taskManager.createTask(CreateTaskParameter.builder()
                .flowCode("xw_test")
                .code(UUID.randomUUID().toString())
                .name("测试任务1")
                .viewer(Lists.newArrayList(AuthGrant.builder()
                        .userId("1")
                        .build(), AuthGrant.builder()
                        .userId("2")
                        .roleId("1")
                        .build()))
                .build()
        );
        if (task.getNextNode() != null) {
            log.debug(task.getNextNode().getName());
        }
        task = taskManager.completeTask(task.getId());
        if (task.getNextNode() != null) {
            log.debug(task.getNextNode().getName());
        }
        task = taskManager.completeTask(task.getId());
        if (task.getNextNode() != null) {
            log.debug(task.getNextNode().getName());
        }
        if (task.getNextNode() != null) {
            log.debug(task.getNextNode().getName());
        }
//        System.out.println(task);
    }

    /**
     * 查找任务
     */
    @Test
    public void findMyTask() {
        //查找我的代办任务
        PageResult myTodoTask = flowManager.getTodoTaskManager().findMyTodoTask(new Query(), new FieldsView());
        System.out.println(myTodoTask);
        PageResult myCompleteTask = flowManager.getMeCompleteTaskManager().findMyCompleteTask(new Query(),new FieldsView());
        System.out.println(myCompleteTask);
        PageResult myViewTask = flowManager.getViewTaskManager().findMyViewTask(new Query(), new FieldsView());
        System.out.println(myViewTask);
    }

    @Test
    public void createFlow() {
        //操作人
        FlowDefinition workFlow = FlowDefinition.builder()
                .code("xw_test")
                .describe("测试流程")
                .name("测试流程")
                .viewer(Sets.newHashSet(AuthGrant.builder()
                        .userId("1")
                        .build(), AuthGrant.builder()
                        .userId("2")
                        .roleId("1")
                        .build()))
                .build();
        //需要先保存一下流程
        workFlow = flowDefinitionService.save(workFlow);
        workFlow.setStart(FlowNode.builder()
                .auth(Lists.newArrayList(AuthGrant.builder()
                        .userId("1")
                        .build(), AuthGrant.builder()
                        .userId("2")
                        .roleId("1")
                        .build()))
                .name("开始")
                .flowId(workFlow.getId())
                .nextNodes(Sets.newHashSet(
                        FlowNode.builder()
                                .auth(Lists.newArrayList(AuthGrant.builder()
                                        .userId("1")
                                        .build(), AuthGrant.builder()
                                        .userId("2")
                                        .roleId("1")
                                        .build()))
                                .name("初审")
                                .flowId(workFlow.getId())
                                .nextNodes(Sets.newHashSet(
                                        FlowNode.builder()
                                                .auth(Lists.newArrayList(AuthGrant.builder()
                                                        .userId("1")
                                                        .build(), AuthGrant.builder()
                                                        .userId("2")
                                                        .roleId("1")
                                                        .build()))
                                                .flowId(workFlow.getId())
                                                .name("复审")
                                                .build()
                                ))
                                .build()
                ))
                .build());
        FlowDefinition save = flowDefinitionService.save(workFlow);
        System.out.println(FastJsonUtils.objectToJson(save));
    }
}

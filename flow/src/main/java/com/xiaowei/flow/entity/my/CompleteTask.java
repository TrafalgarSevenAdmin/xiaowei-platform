package com.xiaowei.flow.entity.my;

import com.xiaowei.flow.constants.TaskStatus;
import lombok.Data;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 自己的已办视图
 */
@Data
@Entity
@Subselect("SELECT \n" +
        "DISTINCT task.id as task_Id,task.name as task_name,task.code as task_code,task.create_user_id as task_create_user_id,task.create_user_name as task_create_user_name,task.ext as task_ext,task.status as task_status,task.update_time as update_time,\n" +
        "flow.id as flow_id,flow.\"code\" as flow_code,flow.\"describe\" as flow_describe,flow.ext as flow_ext,flow.name as flow_name,\n" +
        "next_node.id as next_node_id,next_node.\"code\" as next_node_code,next_node.\"describe\" as next_node_describe,next_node.ext as next_node_ext,next_node.name as next_node_name,\n" +
        "record.operation_user_id as record_operation_user_id,record.operation_user_name as record_operation_user_name\n" +
        "from wf_task task\n" +
        "left join wf_definition flow on task.flow_id = flow.id\n" +
        "left join wf_node_definition next_node on task.next_node_id = next_node.id\n" +
        "left join wf_task_execute_history record on task.now_task_id = record.id")
public class CompleteTask  {
    /**
     * 任务id
     */
    @Id
    String taskId;

    /**
     * 任务名称
     */
    String taskName;

    /**
     * 任务代码
     */
    String taskCode;

    /**
     * 创建此任务的用户
     */
    String taskCreateUserId;

    /**
     * 创建此任务的用户名称
     */
    String taskCreateUserName;

    /**
     * 任务附加数据
     */
    String taskExt;

    /**
     * 任务状态
     */
    TaskStatus taskStatus;

    /**
     * 最后一次更新时间
     */
    Date updateTime;

    /**
     * 所属流程id
     */
    String flowId;

    /**
     * 流程代码
     */
    String flowCode;

    /**
     * 流程描述
     */
    String flowDescribe;

    /**
     * 流程属性数据
     */
    String flowExt;

    /**
     * 流程名称
     */
    String flowName;

    /**
     * 下一节点代码
     */
    String nextNodeCode;

    /**
     * 下一节点描述
     */
    String nextNodeDescribe;

    /**
     * 下一节点扩展属性
     */
    String nextNodeExt;

    /**
     * 下一节点名称
     */
    String nextNodeName;

    /**
     * 操作人
     */
    String recordOperationUserId;

    /**
     * 操作人名称
     */
    String recordOperationUserName;
}
package com.xiaowei.flow.entity.my;

import com.xiaowei.flow.constants.TaskActionType;
import com.xiaowei.flow.constants.TaskStatus;
import lombok.Data;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 我提交的任务视图
 */
@Data
@Entity
@Subselect("SELECT \n" +
        "DISTINCT task.id as task_Id,task.name as task_name,task.code as task_code,task.create_user_id as task_create_user_id,task.create_user_name as task_create_user_name,task.ext as task_ext,task.status as task_status,task.created_time as created_time,task.update_time as update_time,\n" +
        "flow.id as flow_id,flow.\"code\" as flow_code,flow.\"describe\" as flow_describe,flow.ext as flow_ext,flow.name as flow_name,\n" +
        "record.id as record_id,record.action as record_action,record.ext as record_ext,record.operation_user_id as record_operation_user_id,record.operation_user_name as record_operation_user_name,record.reason as record_reason,record.created_time as record_operation_time,\n" +
        "now_node.id as now_node_id,now_node.\"code\" as now_node_code,now_node.\"describe\" as now_node_describe,now_node.ext as now_node_ext,now_node.name as now_node_name,\n" +
        "next_node.id as next_node_id,next_node.\"code\" as next_node_code,next_node.\"describe\" as next_node_describe,next_node.ext as next_node_ext,next_node.name as next_node_name\n" +
        "from wf_task task\n" +
        "left join wf_definition flow on task.flow_id = flow.id\n" +
        "left join wf_task_execute_history record on task.now_task_id = record.id\n" +
        "left join wf_node_definition now_node on record.node_id = now_node.id\n" +
        "left join wf_node_definition next_node on task.next_node_id = next_node.id")
public class SubmitTask {
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
     * 创建时间
     */
    Date createdTime;

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

    /**
     * 操作记录id
     */
    String recordId;

    /**
     * 操作记录状态
     */
    TaskActionType recordAction;

    /**
     * 操作记录存放的业务数据
     */
    String recordExt;

    /**
     * 操作记录操作原因
     */
    String recordReason;

    /**
     * 操作记录时间
     */
    String recordOperationTime;
}
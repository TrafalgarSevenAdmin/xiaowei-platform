package com.xiaowei.flow.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 业务流程执行结果
 */
@Builder
@Data
public class TaskCompleteExtendResult {

    /**
     * 业务指定的下一个节点id
     * 若不为空，则代表此节点的完成后的下一个节点，用于自由跳转/驳回
     */
    String nextNodeId;

    /**
     * 执行完毕后需要保存在任务中的业务属性
     * 一般使用json,并将审核信息记录在其中，
     * 获取使用lastHistory.ext获取上一个保存下来业务属性
     */
    String ext;

    /**
     * 理由
     */
    String reason;

}

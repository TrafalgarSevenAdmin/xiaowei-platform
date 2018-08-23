package com.xiaowei.flow.pojo;

import com.xiaowei.flow.entity.auth.AuthGrant;
import lombok.Builder;
import lombok.Data;

import java.util.*;

/**
 * 创建任务所需要的参数
 */
@Data
@Builder
public class CreateTaskParameter {
    /**
     * 所启动的流程代码
     */
    String flowCode;

    /**
     * 任务名称
     */
    String name;

    /**
     * 任务代码
     */
    String code;

    /**
     * 任务附加数据
     */
    String ext;

    /**
     * 此流程的抄送人员
     * 没有就从默认中获取
     */
    List<AuthGrant> viewer;

    /**
     * 每一个流程的审核人员
     * 没有就从默认中获取
     */
    Map<String, List<AuthGrant>> Auditors;
}

package com.xiaowei.flow.manager;

import com.xiaowei.flow.service.my.ICompleteTaskService;
import com.xiaowei.flow.service.my.ISubmitTaskService;
import com.xiaowei.flow.service.my.ITodoTaskService;
import com.xiaowei.flow.service.my.IViewTaskService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class FlowManager {

    /**
     * 任务处理
     */
    @Autowired
    TaskManager taskManager;

    /**
     * 代办任务检索
     */
    @Autowired
    ITodoTaskService todoTaskManager;

    /**
     * 抄送任务检索
     */
    @Autowired
    IViewTaskService viewTaskManager;

    /**
     * 我操作过的任务检索
     */
    @Autowired
    ICompleteTaskService meCompleteTaskManager;

    /**
     * 我提交的任务检索
     */
    @Autowired
    ISubmitTaskService meSubmitTaskManager;


}

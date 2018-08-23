package com.xiaowei.flow.service.my;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.flow.entity.my.CompleteTask;

public interface ICompleteTaskService extends IBaseService<CompleteTask> {

    /**
     * 查找我参与过的任务
     * @param <T> 若是分页，就使用com.xiaowei.core.result.PageResult 接收
     *           若是不分页，就是用List 接收
     * @param query
     * @param fieldsView
     * @return
     */
    <T> T findMyCompleteTask(Query query, FieldsView fieldsView);
}
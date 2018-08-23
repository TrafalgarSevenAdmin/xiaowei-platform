package com.xiaowei.flow.service.impl.my;

import com.beust.jcommander.internal.Lists;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.flow.constants.TaskStatus;
import com.xiaowei.flow.entity.my.TodoTask;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.repository.my.TodoTaskRepository;
import com.xiaowei.flow.service.my.ITodoTaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service
public class TodoTaskServiceImpl extends BaseServiceImpl<TodoTask> implements ITodoTaskService {

    @Autowired
    TodoTaskRepository todoTaskRepository;

    @Autowired
    LoginUser loginUser;

    public TodoTaskServiceImpl(@Qualifier("todoTaskRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public <T> T findMyTodoTask(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("taskStatus", Filter.Operator.in, Logic.and, Lists.newArrayList(TaskStatus.Running, TaskStatus.Created)));
        query.addFilter(new Filter("userId", Filter.Operator.eq, Logic.or, loginUser.getUserId()));
        if(CollectionUtils.isNotEmpty(loginUser.getRoleIds())) query.addFilter(new Filter("roleId", Filter.Operator.in, Logic.or, loginUser.getRoleIds()));
        if(CollectionUtils.isNotEmpty(loginUser.getDepartmentIds())) query.addFilter(new Filter("departmentId", Filter.Operator.in, Logic.or, loginUser.getDepartmentIds()));

        if (query.isNoPage()) {
            List<TodoTask> todoTasks = this.query(query, TodoTask.class);
            return (T) ObjectToMapUtils.AnyToHandleField(todoTasks,fieldsView);
        } else {
            PageResult pageResult = this.queryPage(query, TodoTask.class);
            pageResult.setRows(ObjectToMapUtils.AnyToHandleField(pageResult.getRows(),fieldsView));
            return (T)pageResult;
        }
    }
}
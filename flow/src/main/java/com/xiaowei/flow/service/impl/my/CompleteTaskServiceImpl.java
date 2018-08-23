package com.xiaowei.flow.service.impl.my;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.flow.entity.my.CompleteTask;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.repository.my.CompleteTaskRepository;
import com.xiaowei.flow.service.my.ICompleteTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service
public class CompleteTaskServiceImpl extends BaseServiceImpl<CompleteTask> implements ICompleteTaskService {


    @Autowired
    CompleteTaskRepository completeTaskRepository;

    @Autowired
    LoginUser loginUser;

    public CompleteTaskServiceImpl(@Qualifier("completeTaskRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public <T> T findMyCompleteTask(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("recordOperationUserId", Filter.Operator.eq, Logic.and, loginUser.getUserId()));
        if (query.isNoPage()) {
            List<CompleteTask> completeTasks = this.query(query, CompleteTask.class);
            return (T) ObjectToMapUtils.AnyToHandleField(completeTasks, fieldsView);
        } else {
            PageResult pageResult = this.queryPage(query, CompleteTask.class);
            pageResult.setRows(ObjectToMapUtils.AnyToHandleField(pageResult.getRows(),fieldsView));
            return (T)pageResult;
        }
    }

}
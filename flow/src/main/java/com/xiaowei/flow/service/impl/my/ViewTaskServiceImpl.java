package com.xiaowei.flow.service.impl.my;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.flow.entity.my.ViewTask;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.repository.my.ViewTaskRepository;
import com.xiaowei.flow.service.my.IViewTaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

@Service
public class ViewTaskServiceImpl extends BaseServiceImpl<ViewTask> implements IViewTaskService {


    @Autowired
    ViewTaskRepository viewTaskRepository;

    @Autowired
    LoginUser loginUser;
    public ViewTaskServiceImpl(@Qualifier("viewTaskRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public <T> T findMyViewTask(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("userId", Filter.Operator.eq, Logic.or, loginUser.getUserId()));
        if(CollectionUtils.isNotEmpty(loginUser.getRoleIds())) query.addFilter(new Filter("roleId", Filter.Operator.in, Logic.or, loginUser.getRoleIds()));
        if(CollectionUtils.isNotEmpty(loginUser.getDepartmentIds())) query.addFilter(new Filter("departmentId", Filter.Operator.in, Logic.or, loginUser.getDepartmentIds()));
        //根据最后操作时间排序
        if (CollectionUtils.isEmpty(query.getSorts())) {
            Sort sort = new Sort();
            sort.setField("updateTime");
            sort.setDir(Sort.Dir.desc);
            query.setSorts(Arrays.asList(sort));
        }

        if (query.isNoPage()) {
            List<ViewTask> todoTasks = this.query(query, ViewTask.class);
            return (T) ObjectToMapUtils.AnyToHandleField(todoTasks, fieldsView);
        } else {
            PageResult pageResult = this.queryPage(query, ViewTask.class);
            pageResult.setRows(ObjectToMapUtils.AnyToHandleField(pageResult.getRows(),fieldsView));
            return (T)pageResult;
        }
    }
}
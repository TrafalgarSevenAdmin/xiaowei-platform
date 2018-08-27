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
import com.xiaowei.flow.entity.my.CompleteTask;
import com.xiaowei.flow.entity.my.SubmitTask;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.repository.my.SubmitTaskRepository;
import com.xiaowei.flow.service.my.ISubmitTaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

@Service
public class SubmitTaskServiceImpl extends BaseServiceImpl<SubmitTask> implements ISubmitTaskService {

    @Autowired
    SubmitTaskRepository submitTaskRepository;

    @Autowired
    LoginUser loginUser;

    public SubmitTaskServiceImpl(@Qualifier("submitTaskRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public <T> T findMySubmitTask(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("taskCreateUserId", Filter.Operator.eq, Logic.and, loginUser.getUserId()));
        //根据最后操作时间排序
        if (CollectionUtils.isEmpty(query.getSorts())) {
            Sort sort = new Sort();
            sort.setField("updateTime");
            sort.setDir(Sort.Dir.desc);
            query.setSorts(Arrays.asList(sort));
        }
        if (query.isNoPage()) {
            List<SubmitTask> completeTasks = this.query(query, SubmitTask.class);
            return (T) ObjectToMapUtils.AnyToHandleField(completeTasks, fieldsView);
        } else {
            PageResult pageResult = this.queryPage(query, SubmitTask.class);
            pageResult.setRows(ObjectToMapUtils.AnyToHandleField(pageResult.getRows(),fieldsView));
            return (T)pageResult;
        }
    }
}
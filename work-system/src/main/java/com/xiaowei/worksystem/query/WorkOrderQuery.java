package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import org.apache.commons.lang3.StringUtils;

public class WorkOrderQuery extends Query {
    private String proposerId;
    private String backgrounderId;
    private String engineerId;
    @Override
    public void generateCondition() {
        //申请处理人过滤
        if(StringUtils.isNoneEmpty(proposerId)){
            addFilter(new Filter("proposer.id", Filter.Operator.eq,proposerId));
        }
        //后台处理人过滤
        if(StringUtils.isNoneEmpty(backgrounderId)){
            addFilter(new Filter("backgrounder.id", Filter.Operator.eq,backgrounderId));
        }
        //处理工程师过滤
        if(StringUtils.isNoneEmpty(engineerId)){
            addFilter(new Filter("engineer.id", Filter.Operator.eq,engineerId));
        }
    }
}

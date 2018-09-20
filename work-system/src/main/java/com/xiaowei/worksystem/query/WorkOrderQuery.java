package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import com.xiaowei.worksystem.status.ServiceType;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Data
public class WorkOrderQuery extends Query {
    private String proposerId;
    private String backgrounderId;
    private String engineerId;
    private String[] userStatus;
    private String[] systemStatus;
    private String code;
    private ServiceType serviceType;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "createdTime");
        //申请处理人过滤
        if(StringUtils.isNotEmpty(proposerId)){
            addFilter(new Filter("proposer.id", Filter.Operator.eq,proposerId));
        }
        //后台处理人过滤
        if(StringUtils.isNotEmpty(backgrounderId)){
            addFilter(new Filter("backgrounder.id", Filter.Operator.eq,backgrounderId));
        }
        //处理工程师过滤
        if(StringUtils.isNotEmpty(engineerId)){
            addFilter(new Filter("engineer.id", Filter.Operator.eq,engineerId));
        }
        //用户状态过滤
        if(ArrayUtils.isNotEmpty(userStatus)){
            addFilter(new Filter("userStatus", Filter.Operator.in,userStatus));
        }
        //后台状态过滤
        if(ArrayUtils.isNotEmpty(systemStatus)){
            addFilter(new Filter("systemStatus", Filter.Operator.in,systemStatus));
        }
        if(StringUtils.isNotEmpty(code)){
            addFilter(new Filter("code", Filter.Operator.eq,code));
        }
        if (serviceType != null) {
            addFilter(new Filter("workOrderType.serviceType", Filter.Operator.eq, serviceType));
        }
    }

}

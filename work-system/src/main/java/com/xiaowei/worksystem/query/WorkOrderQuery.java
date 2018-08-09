package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class WorkOrderQuery extends Query {
    private String proposerId;
    private String backgrounderId;
    private String engineerId;
    private String[] userStatus;
    private String[] systemStatus;

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
    }

    public String getProposerId() {
        return proposerId;
    }

    public void setProposerId(String proposerId) {
        this.proposerId = proposerId;
    }

    public String getBackgrounderId() {
        return backgrounderId;
    }

    public void setBackgrounderId(String backgrounderId) {
        this.backgrounderId = backgrounderId;
    }

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
    }

    public String[] getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String[] userStatus) {
        this.userStatus = userStatus;
    }

    public String[] getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String[] systemStatus) {
        this.systemStatus = systemStatus;
    }
}

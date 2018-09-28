package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class WorkFlowQuery extends Query {
    private String workFlowNameLike;
    private String code;
    private String type;
    private Integer status;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(workFlowNameLike)) {
            addFilter(new Filter("workFlowName", Filter.Operator.like, "%" + workFlowNameLike + "%"));
        }
        if (StringUtils.isNotEmpty(code)) {
            addFilter(new Filter("code", Filter.Operator.eq, code));
        }
        if (StringUtils.isNotEmpty(type)) {
            addFilter(new Filter("type", Filter.Operator.eq, type));
        }
        if (status != null) {
            addFilter(new Filter("status", Filter.Operator.eq, status));
        }
    }
}

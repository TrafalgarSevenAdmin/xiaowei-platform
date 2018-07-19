package com.xiaowei.attendancesystem.query;

import com.xiaowei.attendancesystem.status.ChiefEngineerStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ChiefEngineerQuery extends Query{

    private String chiefNameLike;

    @Override
    public void generateCondition() {
        addFilter(new Filter("status", Filter.Operator.neq, ChiefEngineerStatus.DELETE.getStatus()));
        if (StringUtils.isNotEmpty(chiefNameLike)) {
            addFilter(new Filter("chiefName", Filter.Operator.like, "%" + chiefNameLike + "%"));
        }
    }
}

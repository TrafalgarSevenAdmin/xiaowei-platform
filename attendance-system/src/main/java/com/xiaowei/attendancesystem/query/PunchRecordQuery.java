package com.xiaowei.attendancesystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PunchRecordQuery extends Query {

    private String userId;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("sysUser.id", Filter.Operator.eq, userId));
        }
    }
}

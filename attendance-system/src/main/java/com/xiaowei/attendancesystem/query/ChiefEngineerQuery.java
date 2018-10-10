package com.xiaowei.attendancesystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ChiefEngineerQuery extends Query{

    private String chiefNameLike;
    private String departmentId;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.asc, "chiefName");
        if (StringUtils.isNotEmpty(chiefNameLike)) {
            addFilter(new Filter("chiefName", Filter.Operator.like, "%" + chiefNameLike + "%"));
        }
        if (StringUtils.isNotEmpty(departmentId)) {
            addFilter(new Filter("departments.id", Filter.Operator.eq, departmentId));
        }
    }
}

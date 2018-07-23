package com.xiaowei.attendancesystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PunchRecordQuery extends Query {

    private String userId;
    private String loginName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date b_punchTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date e_punchTime;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "createdTime");
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("sysUser.id", Filter.Operator.eq, userId));
        }
        //根据打卡人名称过滤
        if (StringUtils.isNotEmpty(loginName)) {
            addFilter(new Filter("sysUser.loginName", Filter.Operator.like, Logic.or, "%" + loginName + "%"));
            addFilter(new Filter("sysUser.nickName", Filter.Operator.like, Logic.or, "%" + loginName + "%"));
        }
        //根据打卡时间范围过滤
        if (b_punchTime != null && e_punchTime != null) {
            addFilter(new Filter("punchDate", Filter.Operator.between, b_punchTime, e_punchTime));
        }

    }
}

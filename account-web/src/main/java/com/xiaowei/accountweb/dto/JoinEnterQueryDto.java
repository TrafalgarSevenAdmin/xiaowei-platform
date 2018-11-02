package com.xiaowei.accountweb.dto;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@ApiModel("入驻申请单筛选")
@Data
public class JoinEnterQueryDto extends Query {

    @ApiModelProperty("用户名")
    String userName;

    @ApiModelProperty("手机号")
    String mobilePhone;

    @ApiModelProperty(value = "审核是否通过",notes = "默认查还没有审核的数据，即此值唯恐")
    Boolean auditPass = null;


    @Override
    public void generateCondition() {

        if (StringUtils.isNotEmpty(mobilePhone)) {
            addFilter(new Filter("mobilePhone", Filter.Operator.eq, mobilePhone));
        }

        if (StringUtils.isNotEmpty(userName)) {
            addFilter(new Filter("userName", Filter.Operator.eq, userName));
        }

        if (auditPass != null) {
            addFilter(new Filter("auditPass", Filter.Operator.eq, auditPass));
        } else {
            addFilter(Filter.isNull("auditPass"));
        }
    }
}

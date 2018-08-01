package com.xiaowei.commondict.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mocker
 * @Date 2018-03-21 15:11:05
 * @Description 系统权限
 * @Version 1.0
 */
@Data
public class DictionaryQuery extends Query {
    private String code;
    private String ownCode;
    private String codeLike;
    private String name;
    private String parentId;
    private Integer level;


    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(code)) {
            this.addFilter(Filter.eq("code", code));
        }
        if (StringUtils.isNotEmpty(ownCode)) {
            this.addFilter(Filter.eq("ownCode", ownCode));
        }
        if (StringUtils.isNotEmpty(name)) {
            this.addFilter(Filter.eq("name", name));
        }
        if (StringUtils.isNotEmpty(codeLike)) {
            this.addFilter(Filter.like("code", codeLike + "_%"));
        }
        if (StringUtils.isNotEmpty(parentId)) {
            this.addFilter(Filter.eq("parentId", parentId));
        }
        if (level != null) {
            this.addFilter(Filter.eq("level", level));
        }
    }

}

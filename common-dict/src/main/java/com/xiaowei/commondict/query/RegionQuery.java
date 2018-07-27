package com.xiaowei.commondict.query;

import com.xiaowei.commondict.region.RegionUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhouyang
 * @Date 2017-09-06 18:03
 * @Description
 * @Version 1.0
 */
@Data
public class RegionQuery extends Query {

    private String mergerName;
    private String code;
    private String name;
    private String shortCode;
    private String parentCode;
    private Sort.Dir nameSort;

    @Override
    public void generateCondition() {
        //根据用户名排序
        if (Sort.Dir.asc.equals(nameSort)) {
            addSort(Sort.Dir.asc, "name");
        } else {
            addSort(Sort.Dir.desc, "name");
        }
        if (!StringUtils.isEmpty(mergerName)) {
            this.addFilter(Filter.eq("mergerName", mergerName));
        }
        if (!StringUtils.isEmpty(code)) {
            this.addFilter(Filter.eq("code", RegionUtils.completed(code)));
        }
        if (!StringUtils.isEmpty(name)) {
            this.addFilter(Filter.eq("name", name));
        }
        if (!StringUtils.isEmpty(shortCode)) {
            this.addFilter(Filter.eq("shortCode", shortCode));
        }
        if (!StringUtils.isEmpty(parentCode)) {
            this.addFilter(Filter.eq("parentShortCode", parentCode));
        }

    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}

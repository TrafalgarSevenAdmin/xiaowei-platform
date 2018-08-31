package com.xiaowei.commondict.query;

import com.hankcs.hanlp.tokenizer.StandardTokenizer;
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
    private String mergerNameLike;
    private Sort.Dir levelSort;
    private String mergerNameSplit;

    @Override
    public void generateCondition() {
        if (Sort.Dir.asc.equals(levelSort)) {
            addSort(Sort.Dir.asc, "level");
        } else if (Sort.Dir.desc.equals(levelSort)) {
            addSort(Sort.Dir.desc, "level");
        }
        //根据名称排序
        if (Sort.Dir.asc.equals(nameSort)) {
            addSort(Sort.Dir.asc, "name");
        } else if (Sort.Dir.desc.equals(nameSort)) {
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
        if (!StringUtils.isEmpty(mergerNameLike)) {
            this.addFilter(Filter.like("mergerName", "%" + mergerNameLike + "%"));
        }
        if (!StringUtils.isEmpty(mergerNameSplit)) {
            StandardTokenizer.segment(mergerNameSplit).stream().map(term -> term.word)
                    .distinct().forEach(word ->{
                System.out.println(word);
                this.addFilter(Filter.like("mergerName", "%" + word + "%"));
            });
        }

    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}

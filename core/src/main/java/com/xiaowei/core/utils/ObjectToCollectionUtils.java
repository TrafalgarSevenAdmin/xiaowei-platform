package com.xiaowei.core.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectToCollectionUtils {


    public static Set objectToSet(List<Map<String,Object>> data,String field){
        Set result = new HashSet<>();
        if(!CollectionUtils.isEmpty(data) && !StringUtils.isEmpty(field)){
            data.stream().forEach(d -> {
                Object o = d.get(field);
                if(o != null && !StringUtils.isEmpty(o.toString())){
                    result.add(o);
                }
            });
        }
        return result;
    }
}

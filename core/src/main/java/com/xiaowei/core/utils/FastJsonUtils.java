package com.xiaowei.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

public class FastJsonUtils {

    public static String objectToJson(Object object){
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public static <T> T jsonToObject(Object object,Class<T> type){
        return JSON.parseObject(objectToJson(object), type);
    }

    public static <T> List<T> objectToList(Object collection, Class<T> collectionClass){
        return JSONArray.parseArray(objectToJson(collection), collectionClass);
    }
}

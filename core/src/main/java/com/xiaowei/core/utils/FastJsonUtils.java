package com.xiaowei.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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

    public static List<JSONObject> listObjectToListJsonObject(List objects){
        List<JSONObject> jsonObjects = new ArrayList<>();
        objects.stream().forEach(obj -> {
            jsonObjects.add(JSONObject.parseObject(JSONObject.toJSONString(obj)));
        });
        return jsonObjects;
    }

    public static <T> List<T>  listJsonObjectToListObject(List<JSONObject> jsonObjects, Class<T> collectionClass){
        List<T> objects = new ArrayList<>();
        jsonObjects.stream().forEach(jsonObject -> {
            objects.add(jsonObject.toJavaObject(collectionClass));
        });
        return objects;
    }

    public static List<JSONObject> transArrToArrMap(List<String> arrs, String key, List<JSONObject> jsonObjects) {
        if(CollectionUtils.isEmpty(arrs)){
            return jsonObjects;
        }
        List<JSONObject> newJsonObjects = new ArrayList<>();
        jsonObjects.stream().forEach(jsonObject -> {
            arrs.stream().forEach(arr -> {
                JSONObject copy = JSONObject.parseObject(jsonObject.toJSONString());
                newJsonObjects.add(copy.fluentPut(key,arr));
            });
        });
        return newJsonObjects;
    }

}

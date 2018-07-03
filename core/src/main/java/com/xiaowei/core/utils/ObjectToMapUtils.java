package com.xiaowei.core.utils;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.result.FieldsView;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author mocker
 * @Date 2018-03-27 10:37:07
 * @Description 对象转Map工具
 * @Version 1.0
 */
public class ObjectToMapUtils {


    /**
     * 集合对象转集合Map
     *
     * @param objs          需要转换的集合对象
     * @param includeFields 包含的字段
     * @return
     */
    @SuppressWarnings("all")
    public static List<Map<String, Object>> listToIncludeFieldMap(Collection collection, String[] includeFields) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if(CollectionUtils.isEmpty(collection)){
            return dataList;
        }
        for (Object o : collection) {
            dataList.add(objectToIncludeFieldMap(o,includeFields));
        }
        return dataList;
    }

    /**
     * 集合对象转集合Map
     *
     * @param objs          需要转换的集合对象
     * @param includeFields 不包含的字段
     * @return
     */
    @SuppressWarnings("all")
    public static List<Map<String, Object>> listToExcludeFieldMap(Collection collection, String[] excludeFields) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if(CollectionUtils.isEmpty(collection)){
            return dataList;
        }
        for (Object o : collection) {
            dataList.add(objectToExcludeFieldMap(o,excludeFields));
        }
        return dataList;
    }

    public static List<Map<String, Object>> listToMap(Collection collection, FieldsView fieldsView) {
        if(CollectionUtils.isEmpty(collection)){
            return new ArrayList<>();
        }
        if(fieldsView.isInclude()){
            return listToIncludeFieldMap(collection,fieldsView.getFields().toArray(new String[0]));
        }else{
            return listToExcludeFieldMap(collection,fieldsView.getFields().toArray(new String[0]));
        }
    }


    /**
     * 对象转map
     *
     * @param obj           需要转换的对象
     * @param includeFields 包含的字段
     * @return
     */
    public static Map<String, Object> objectToIncludeFieldMap(Object obj, String[] includeFields) {
        if(obj==null){
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (ArrayUtils.contains(includeFields, propertyDescriptor.getName()) && !"class".equals(propertyDescriptor.getName())) {
                Method method = propertyDescriptor.getReadMethod();
                if(method.getAnnotation(JsonIgnore.class) == null){
                    try {
                        Object result = method.invoke(obj);
                        data.put(propertyDescriptor.getName(), result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return data;
    }


    /**
     * 对象转map
     * @param obj           需要转换的对象
     * @param excludeFields 不包含的字段
     * @return
     */
    public static Map<String, Object> objectToExcludeFieldMap(Object obj, String[] excludeFields) {
        if(obj==null){
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (excludeFields != null && !ArrayUtils.contains(excludeFields, propertyDescriptor.getName())
                    && !"class".equals(propertyDescriptor.getName())) {
                Method method = propertyDescriptor.getReadMethod();
                if(method.getAnnotation(JsonIgnore.class) == null) {
                    try {
                        Object result = method.invoke(obj);
                        data.put(propertyDescriptor.getName(), result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return data;
    }

    public static Map<String, Object> objectToMap(Object obj, FieldsView fieldsView) {
        if(fieldsView.isInclude()){
            return objectToIncludeFieldMap(obj,fieldsView.getFields().toArray(new String[0]));
        }else{
            return objectToExcludeFieldMap(obj,fieldsView.getFields().toArray(new String[0]));
        }
    }
}

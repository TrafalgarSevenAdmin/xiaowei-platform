package com.xiaowei.core.utils;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;
import com.xiaowei.core.result.FieldsView;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
        if (CollectionUtils.isEmpty(collection)) {
            return dataList;
        }
        for (Object o : collection) {
            dataList.add(objectToIncludeFieldMap(o, includeFields));
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
        if (CollectionUtils.isEmpty(collection)) {
            return dataList;
        }
        for (Object o : collection) {
            dataList.add(objectToExcludeFieldMap(o, excludeFields));
        }
        return dataList;
    }

    public static List<Map<String, Object>> listToMap(Collection collection, FieldsView fieldsView) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        if (fieldsView.isInclude()) {
            return listToIncludeFieldMap(collection, fieldsView.getFields().toArray(new String[0]));
        } else {
            return listToExcludeFieldMap(collection, fieldsView.getFields().toArray(new String[0]));
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
        if (obj == null) {
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (ArrayUtils.contains(includeFields, propertyDescriptor.getName()) && !"class".equals(propertyDescriptor.getName())) {
                Method method = propertyDescriptor.getReadMethod();
                if (method.getAnnotation(JsonIgnore.class) == null) {
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
     *
     * @param obj           需要转换的对象
     * @param excludeFields 不包含的字段
     * @return
     */
    public static Map<String, Object> objectToExcludeFieldMap(Object obj, String[] excludeFields) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (excludeFields != null && !ArrayUtils.contains(excludeFields, propertyDescriptor.getName())
                    && !"class".equals(propertyDescriptor.getName())) {
                Method method = propertyDescriptor.getReadMethod();
                if (method.getAnnotation(JsonIgnore.class) == null) {
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
        if (fieldsView.isInclude()) {
            return objectToIncludeFieldMap(obj, fieldsView.getFields().toArray(new String[0]));
        } else {
            return objectToExcludeFieldMap(obj, fieldsView.getFields().toArray(new String[0]));
        }
    }

    /**
     * 忽略的字段缓存
     */
    private static Map<Class, String[]> ignoresFieldCache = new HashMap<>();

    /**
     * 任何对象去配置的需要包含或不需要包含的对象/字段
     * 带层级关系
     *
     * @param obj  需要转换的对象
     * @param view 配置的字段
     * @return
     */
    public static <T> T anyToHandleField(Object obj, FieldsView view) {
        return anyToHandleField(obj, view.getFields().toArray(new String[0]), view.isInclude(), null);
    }

    public static <T> T anyToHandleField(Object obj, String[] fields, boolean include, String parent) {
        if (obj == null) {
            return null;
        }
        //若是集合
        if (obj instanceof Collection) {
            ArrayList<Object> objects = new ArrayList<>();
            for (Object o : ((Collection) obj)) {
                objects.add(anyToHandleField(o, fields, include, parent));
            }
            return (T) objects;
        } else {
            //不是集合
            Map<String, Object> data = new HashMap<>();
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
            Set<String> ignores = Sets.newHashSet();

            if (ignoresFieldCache.get(obj.getClass()) == null) {
                JsonIgnoreProperties annotation = obj.getClass().getAnnotation(JsonIgnoreProperties.class);
                if (annotation != null) ignores.addAll(Arrays.asList(annotation.value()));
                ignores.addAll(Arrays.stream(propertyDescriptors).filter(p -> p.getReadMethod().getAnnotation(JsonIgnore.class) != null).map(PropertyDescriptor::getName).collect(Collectors.toList()));
                ignores.addAll(Arrays.stream(obj.getClass().getFields()).filter(o -> o.getAnnotation(JsonIgnore.class) != null).map(v -> v.getName()).collect(Collectors.toList()));
                ignoresFieldCache.put(obj.getClass(), ignores.toArray(new String[0]));
            }else {
                ignores.addAll(Arrays.asList(ignoresFieldCache.get(obj.getClass())));
            }

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String prefix = (StringUtils.isNotEmpty(parent) ? (parent + ".") : "") + propertyDescriptor.getName();
                if (include && !Arrays.stream(fields).filter(s -> s.startsWith(prefix)).findFirst().isPresent()) {
                    //包含，但包含的字段不存在
                    continue;
                }
                if (!include && ArrayUtils.contains(fields, prefix)) {
                    //不包含，但不包含的字段存在
                    continue;
                }
                if (fields != null
                        && !"class".equals(propertyDescriptor.getName())) {
                    Method method = propertyDescriptor.getReadMethod();
                    try {
                        //不是忽略字段
                        if (!ignores.contains(propertyDescriptor.getName())) {
                            Object result = method.invoke(obj);
                            Optional<String> first = Arrays.stream(fields).filter(s -> s.startsWith(prefix)).findFirst();
                            if (first.isPresent() && !first.get().equals(prefix)) {
                                //还需要进入下一层中处理
                                result = anyToHandleField(result, fields, include, prefix);
                            }
                            data.put(propertyDescriptor.getName(), result);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return (T) data;
        }
    }
}

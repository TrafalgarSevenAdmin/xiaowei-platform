package com.xiaowei.core.query.rundi.query;

import java.io.Serializable;

/**
 * @author zhouyang
 * @Date 2017-03-15 下午3:55
 * @Description 排序
 * @Version 1.0
 */
public class Sort implements Serializable {

    public enum Dir{
        desc("desc"),

        asc("asc");

        private String dir;

        Dir(String dir) {
            this.dir = dir;
        }
    }

    private String field;

    private Dir dir;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}

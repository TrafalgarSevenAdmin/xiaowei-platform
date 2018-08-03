package com.xiaowei.socketjscore.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SocketCoreBean implements Serializable {
    //推送标题
    private String title;
    //推送内容
    private Object data;
    //推送目标对象id
    private String object_id;
    //推送类型
    private Integer type;
    //推送时间
    private Date pushTime;

    public SocketCoreBean(String title, Object data, String object_id, Integer type, Date pushTime) {
        this.title = title;
        this.data = data;
        this.object_id = object_id;
        this.type = type;
        this.pushTime = pushTime;
    }
}

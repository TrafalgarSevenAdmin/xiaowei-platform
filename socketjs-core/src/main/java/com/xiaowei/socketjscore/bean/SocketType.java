package com.xiaowei.socketjscore.bean;

public enum SocketType {
    /**
     * 有新的服务项目需要审核
     */
    TOAUDIT(0),
    /**
     * 服务项目审核已经通过
     */
    COMPLETEAUDIT(1);

    Integer type;

    SocketType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

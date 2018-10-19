package com.xiaowei.attendancesystem.status;

public enum PunchType {
    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 工单外出打卡
     */
    OUTER("外出");

    private String type;

    PunchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

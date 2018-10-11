package com.xiaowei.attendancesystem.status;

public enum PunchRecordType {
    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 迟到
     */
    BELATE("迟到"),

    /**
     * 未打卡
     */
    CLOCKISNULL("未打卡"),

    /**
     * 异常
     */
    EXCEPTION("异常"),

    /**
     * 没有记录
     */
    NOPUNCH("没有记录");

    private String value;

    PunchRecordType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

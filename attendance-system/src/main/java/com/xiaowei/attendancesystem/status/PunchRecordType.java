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
     * 上班未打卡
     */
    CLOCKINISNULL("上班未打卡"),

    /**
     * 下班未打卡
     */
    CLOCKOUTISNULL("下班未打卡"),

    /**
     * 上下班均未打卡
     */
    CLOCKINISNULLANDCLOCKOUTISNULL("上下班均未打卡"),

    /**
     * 迟到且下班未打卡
     */
    BELATEANDCLOCKOUTISNULL("迟到且下班未打卡"),

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

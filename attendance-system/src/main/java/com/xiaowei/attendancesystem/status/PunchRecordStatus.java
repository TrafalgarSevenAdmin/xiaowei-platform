package com.xiaowei.attendancesystem.status;

public enum PunchRecordStatus {
    NORMAL("正常"),//正常打卡
    EXCEPTION("异常"),//异常打卡
    BELATE("迟到"),//迟到
    NOPUNCH("没有记录"),//没有记录
    CLOCKISNULL("未打卡");//未打卡
    private String value;
    PunchRecordStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

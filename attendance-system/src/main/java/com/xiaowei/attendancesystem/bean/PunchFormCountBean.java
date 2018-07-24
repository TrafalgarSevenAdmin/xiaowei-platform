package com.xiaowei.attendancesystem.bean;

import lombok.Data;

@Data
public class PunchFormCountBean {
    private Long belateCount;
    private Long clockInIsNullCount;
    private Long clockOutIsNullCount;

    public PunchFormCountBean(Long belateCount, Long clockInIsNullCount, Long clockOutIsNullCount) {
        this.belateCount = belateCount;
        this.clockInIsNullCount = clockInIsNullCount;
        this.clockOutIsNullCount = clockOutIsNullCount;
    }

    public PunchFormCountBean() {
    }
}

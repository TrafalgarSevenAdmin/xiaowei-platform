package com.xiaowei.attendancesystem.service;

import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.core.basic.service.IBaseService;

public interface IPunchRecordService extends IBaseService<PunchRecord> {

    PunchRecord savePunchRecord(PunchRecord punchRecord, Geometry shape);
}

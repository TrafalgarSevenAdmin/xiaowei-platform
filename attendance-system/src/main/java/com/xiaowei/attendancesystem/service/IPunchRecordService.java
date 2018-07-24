package com.xiaowei.attendancesystem.service;

import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.core.basic.service.IBaseService;

import java.util.Date;
import java.util.List;

public interface IPunchRecordService extends IBaseService<PunchRecord> {

    PunchRecord savePunchRecord(PunchRecord punchRecord, Geometry shape);

    List<PunchRecord> findByCompanyIdAndMonth(String companyId, Date selectMonth) throws Exception;
}

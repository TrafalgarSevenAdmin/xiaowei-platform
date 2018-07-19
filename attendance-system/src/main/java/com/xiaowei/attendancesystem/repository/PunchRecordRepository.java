package com.xiaowei.attendancesystem.repository;

import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface PunchRecordRepository extends BaseRepository<PunchRecord>{

    /**
     * 获取指定打卡人的当前日期的打卡记录
     * @param userId
     * @return
     */
    @Query("select p from PunchRecord p where p.sysUser.id = ?1 and p.punchDate = CURRENT_DATE")
    PunchRecord findByUserIdAndCurrentDate(String userId);
}

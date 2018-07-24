package com.xiaowei.attendancesystem.repository;

import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PunchRecordRepository extends BaseRepository<PunchRecord>{

    /**
     * 获取指定打卡人的当前日期的打卡记录
     * @param userId
     * @return
     */
    @Query("select p from PunchRecord p where p.sysUser.id = ?1 and p.punchDate = CURRENT_DATE")
    PunchRecord findByUserIdAndCurrentDate(String userId);

    /**
     * 获取指定打卡人指定时间的打卡记录
     * @param userIds
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @return
     */@Query("select p from PunchRecord p where p.sysUser.id in ?1 and p.punchDate between ?2 and ?3")
    List<PunchRecord> findByUserIdsBetweenPunchTime(Set<String> userIds, Date firstDayOfMonth, Date lastDayOfMonth);
}

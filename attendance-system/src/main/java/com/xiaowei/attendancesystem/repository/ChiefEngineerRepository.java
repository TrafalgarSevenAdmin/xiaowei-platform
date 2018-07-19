package com.xiaowei.attendancesystem.repository;

import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChiefEngineerRepository extends BaseRepository<ChiefEngineer>{

    @Query(value = "SELECT * from A_CHIEFENGINEER c LEFT JOIN A_CHIEF_DEPARTMENT cd on c.id = cd.chief_id " +
            "LEFT JOIN SYS_DEPARTMENT d on cd.department_id = d.id " +
            "LEFT JOIN SYS_USER_DEPARTMENT ud on d.id = ud.department_id where ud.user_id = ?1",nativeQuery = true)
    List<ChiefEngineer> findByUserId(String userId);
}

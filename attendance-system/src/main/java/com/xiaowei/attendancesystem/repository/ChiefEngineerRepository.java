package com.xiaowei.attendancesystem.repository;

import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChiefEngineerRepository extends BaseRepository<ChiefEngineer>{

    @Query(value = "SELECT * from A_CHIEFENGINEER c LEFT JOIN A_CHIEF_DEPARTMENT cd on c.id = cd.chief_id " +
            "LEFT JOIN sys_user u on cd.department_id = u.department_id where u.id  = ?1",nativeQuery = true)
    List<ChiefEngineer> findByUserId(String userId);
}

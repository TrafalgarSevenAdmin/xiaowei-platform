package com.xiaowei.account.repository;


import com.xiaowei.account.entity.Department;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends BaseRepository<Department> {

    @Query("select d.id from Department d where d.company.id = ?1")
    List<String> findIdsByCompanyId(String companyId);
}

package com.xiaowei.account.service;


import com.xiaowei.account.entity.Department;
import com.xiaowei.core.basic.service.IBaseService;

public interface IDepartmentService extends IBaseService<Department> {

    Department saveDepartment(Department department);

    Department updateDepartment(Department department);

    Department updateStatus(Department department);
}

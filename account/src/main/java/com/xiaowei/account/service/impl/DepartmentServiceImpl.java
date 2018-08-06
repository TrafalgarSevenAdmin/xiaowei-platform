package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.DepartmentStatus;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.Department;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.repository.DepartmentRepository;
import com.xiaowei.account.service.IDepartmentService;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 公司服务
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department> implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public DepartmentServiceImpl(@Qualifier("departmentRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Department saveDepartment(Department department) {
        //判定参数是否合规
        judgeAttribute(department, JudgeType.INSERT);
        departmentRepository.save(department);
        return department;
    }

    private void judgeAttribute(Department department, JudgeType judgeType) {
        if(judgeType.equals(JudgeType.INSERT)){//保存
            department.setId(null);
            //设置code
            department.setCode(UUID.randomUUID().toString());
            //设置创建时间
            department.setCreatedTime(new Date());
            //默认状态正常
            department.setStatus(DepartmentStatus.NORMAL.getStatus());
        }else if(judgeType.equals(JudgeType.UPDATE)){//修改
            String departmentId = department.getId();
            EmptyUtils.assertString(departmentId,"没有传入对象id");
            Department one = departmentRepository.getOne(departmentId);
            EmptyUtils.assertObject(one,"没有查询到需要修改的对象");

            //修改部门判断当前登录用户是否拥有被修改的部门的权限
            if(!LoginUserUtils.hasDepartmentId(departmentId)){
                throw new UnauthorizedException("保存失败:没有权限修改该部门");
            }
            //设置不允许在此处修改的属性
            department.setStatus(one.getStatus());
        }
        //验证所属公司
        EmptyUtils.assertObject(department.getCompany(), "所属公司为空");
        EmptyUtils.assertString(department.getCompany().getId(), "所属公司id为空");
        Optional<Company> company = companyRepository.findById(department.getCompany().getId());
        EmptyUtils.assertOptional(company,"没有查询到所属公司");
    }

    @Override
    @Transactional
    public Department updateDepartment(Department department) {
        //判定参数是否合规
        judgeAttribute(department, JudgeType.UPDATE);
        departmentRepository.save(department);
        return department;
    }

    @Override
    @Transactional
    public Department updateStatus(Department department) {
        String departmentId = department.getId();
        EmptyUtils.assertString(departmentId,"没有传入对象id");
        Optional<Department> optional = departmentRepository.findById(departmentId);
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        Department one = optional.get();
        EmptyUtils.assertObject(one,"没有查询到需要删除的对象");
        //删除公司判断当前登录用户是否拥有被删除的公司的权限
        if(!LoginUserUtils.hasDepartmentId(departmentId)){
            throw new UnauthorizedException("保存失败:没有权限删除该部门");
        }
        one.setStatus(department.getStatus());
        departmentRepository.save(one);
        return one;
    }
}

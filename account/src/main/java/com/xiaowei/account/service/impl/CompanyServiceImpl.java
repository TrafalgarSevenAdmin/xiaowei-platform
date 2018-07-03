package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.CompanyStatus;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.service.ICompanyService;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.validate.JudgeType;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 公司服务
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<Company> implements ICompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(@Qualifier("companyRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Company saveCompany(Company company) {
        //判定参数是否合规
        judgeAttribute(company, JudgeType.INSERT);
        companyRepository.save(company);
        return company;
    }

    @Override
    @Transactional
    public Company updateCompany(Company company) {
        //判定参数是否合规
        judgeAttribute(company, JudgeType.UPDATE);
        companyRepository.save(company);
        return company;
    }

    private void judgeAttribute(Company company, JudgeType judgeType) {
        //判断name是否唯一
        String companyName = company.getCompanyName();
        if (StringUtils.isEmpty(companyName)) {
            throw new BusinessException("保存失败:公司名称为空");
        }

        if (judgeType.equals(JudgeType.INSERT)) {//保存
            company.setId(null);
            Company byName = companyRepository.findByCompanyName(companyName);
            if (byName != null) {
                throw new BusinessException("保存失败:公司名称重复");
            }
            //设置code
            company.setCode(UUID.randomUUID().toString());
            //设置创建时间
            company.setCreatedTime(new Date());
            //默认状态正常
            company.setStatus(CompanyStatus.NORMAL.getStatus());

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String companyId = company.getId();

            if (StringUtils.isEmpty(companyId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            Company one = companyRepository.getOne(companyId);
            if (one == null) {
                throw new BusinessException("保存失败:没有查询到需要修改的对象");
            }

            //修改公司判断当前登录用户是否拥有被修改的公司的权限
            if(!LoginUserUtils.hasCompanyId(companyId)){
                throw new UnauthorizedException("保存失败:没有权限修改该公司");
            }

            //如果名称没有修改,则不发sql验证,否则发送sql验证name唯一性
            if (!one.getCompanyName().equals(companyName)) {
                Company byName = companyRepository.findByCompanyName(companyName);
                if (byName != null) {
                    throw new BusinessException("保存失败:公司名称重复");
                }
            }
            //设置不允许在此处修改的属性
            company.setStatus(one.getStatus());

        }
    }

    @Override
    @Transactional
    public void deleteCompany(String companyId) {
        if (StringUtils.isEmpty(companyId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Company one = companyRepository.getOne(companyId);
        if (one == null) {
            throw new BusinessException("删除失败:没有查询到需要删除的对象");
        }
        //删除公司判断当前登录用户是否拥有被删除的公司的权限
        if(!LoginUserUtils.hasCompanyId(companyId)){
            throw new UnauthorizedException("保存失败:没有权限删除该公司");
        }
        companyRepository.deleteById(companyId);
    }

    @Override
    @Transactional
    public Company updateStatus(Company company) {
        String companyId = company.getId();
        if (StringUtils.isEmpty(companyId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Company one = companyRepository.getOne(companyId);
        if (one == null) {
            throw new BusinessException("删除失败:没有查询到需要删除的对象");
        }
        //删除公司判断当前登录用户是否拥有被删除的公司的权限
        if(!LoginUserUtils.hasCompanyId(companyId)){
            throw new UnauthorizedException("保存失败:没有权限删除该公司");
        }
        one.setStatus(company.getStatus());
        companyRepository.save(one);
        return one;
    }
}

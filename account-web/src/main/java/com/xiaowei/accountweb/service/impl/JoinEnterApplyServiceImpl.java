package com.xiaowei.accountweb.service.impl;

import com.xiaowei.account.consts.CompanyStatus;
import com.xiaowei.account.consts.PlatformTenantConst;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.entity.Tenement;
import com.xiaowei.account.service.*;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.JoinAuditDto;
import com.xiaowei.accountweb.entity.JoinEnterApply;
import com.xiaowei.accountweb.repository.JoinEnterApplyRepository;
import com.xiaowei.accountweb.service.IJoinEnterApplyService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.utils.StringPYUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class JoinEnterApplyServiceImpl extends BaseServiceImpl<JoinEnterApply> implements IJoinEnterApplyService {


    @Autowired
    JoinEnterApplyRepository joinEnterApplyRepository;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    ISysRoleService roleService;

    @Autowired
    ITenementService tenementService;

    @Autowired
    ICompanyService companyService;

    public JoinEnterApplyServiceImpl(@Qualifier("joinEnterApplyRepository")BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public void audit(JoinAuditDto joinAuditDto)  {
        JoinEnterApply joinEnterApply = joinEnterApplyRepository.findById(joinAuditDto.getId()).orElseThrow(() -> new BusinessException("没有找到对应的申请单信息"));
        joinEnterApply.setAuditPass(joinAuditDto.getPass());
        joinEnterApply.setAuditReason(joinAuditDto.getReason());
        joinEnterApply.setAuditTime(new Date());
        joinEnterApply.setAuditUser(sysUserService.getOne(LoginUserUtils.getLoginUser().getId()));
        //如果申请通过
        if (joinAuditDto.getPass()) {
            switch (joinAuditDto.getJoinType()) {
                case Company:
                    //若是公司加盟
                    auditCompanyJoin(joinEnterApply,joinAuditDto.getCompanyJoinApply());
                    break;
                case Engineer:
                    //若是工程师加盟
                    auditEngineerJoin(joinEnterApply,joinAuditDto.getEngineerJoinApply());
                break;
                case Default:
                    if (StringUtils.isNotEmpty(joinEnterApply.getTenancyId())) {
                        auditEngineerJoin(joinEnterApply, joinAuditDto.getEngineerJoinApply());
                    } else {
                        auditCompanyJoin(joinEnterApply,joinAuditDto.getCompanyJoinApply());
                    }
                    break;
            }
        }
        joinEnterApplyRepository.save(joinEnterApply);
        //给用户发送通知
    }

    /**
     * 工程师加盟分配信息
     * @param joinEnterApply
     * @param userJoinApply
     */
    private void auditEngineerJoin(JoinEnterApply joinEnterApply, JoinAuditDto.EngineerJoinApply userJoinApply) {
        //首先注册用户
        SysUser sysUser = new SysUser();
        sysUser.setCard(joinEnterApply.getCardNumber());
        sysUser.setMobile(joinEnterApply.getMobilePhone());
        sysUser.setEmail(joinEnterApply.getEmail());
        sysUser.setLoginName(joinEnterApply.getMobilePhone());
        sysUser.setNickName(joinEnterApply.getUserName());
        sysUser.setSubWechat(true);
        //由于限制只能分配审核人员可以看到的部门，因此此部门只会属于该租户或托管角色
        sysUser.setDepartment(departmentService.findById(userJoinApply.getDepartmentId()));
        //由于限制只能分配审核人员可以看到的角色，因此此角色只会属于该租户或托管角色
        sysUser.setRoles(roleService.findByIds(userJoinApply.getRoleIds().toArray(new String[]{})));
        //注册用户
        SysUser targetUser = sysUserService.registerUser(sysUser);
        //数据更新
        joinEnterApply.setTargetUser(targetUser);
        //todo 绑定微信？推送消息？？

    }

    /**
     * 公司加盟分配信息
     * @param joinEnterApply
     * @param companyJoinApply
     */
    private void auditCompanyJoin(JoinEnterApply joinEnterApply, JoinAuditDto.CompanyJoinApply companyJoinApply) {
        //非平台用户不能审核公司加盟的信息。
        if (!LoginUserUtils.getLoginUser().getTenancyId().equals(PlatformTenantConst.ID)) {
            throw new BusinessException("非平台用户不能审核公司加盟的信息！");
        }

        //创建租户
        Tenement tenement = new Tenement();
        tenement.setName(companyJoinApply.getCompanyName());
        tenement.setCode(StringPYUtils.getSpellCode(companyJoinApply.getCompanyName()));
        tenement = tenementService.saveTenement(tenement);

        //创建公司
        Company company = new Company();
        company.setAddress(joinEnterApply.getAddr());
        company.setCompanyName(companyJoinApply.getCompanyName());
        company.setCode(StringPYUtils.getSpellCode(companyJoinApply.getCompanyName()));
        company.setTenancyId(tenement.getId());
        company.setStatus(CompanyStatus.NORMAL.getStatus());
        company = companyService.save(company);

        //创建用户，并绑定此租户的超级管理员。
        SysUser sysUser = new SysUser();
        sysUser.setTenancyId(tenement.getId());
        sysUser.setCard(joinEnterApply.getCardNumber());
        sysUser.setMobile(joinEnterApply.getMobilePhone());
        sysUser.setEmail(joinEnterApply.getEmail());
        sysUser.setLoginName(joinEnterApply.getMobilePhone());
        sysUser.setNickName(joinEnterApply.getUserName());
        sysUser.setSubWechat(true);
        //分配默认的租户管理员
        sysUser.setRoles(roleService.query(new Query().addFilter(Filter.eq("code", PlatformTenantConst.TENEMENT_ADMIN_ROLE_CODE))));
        sysUser.setCompany(company);
        sysUser = sysUserService.registerUser(sysUser);
        company.setPrincipal(sysUser);
        //更新公司的管理人
        companyService.save(company);
        joinEnterApply.setTargetUser(sysUser);
    }
}

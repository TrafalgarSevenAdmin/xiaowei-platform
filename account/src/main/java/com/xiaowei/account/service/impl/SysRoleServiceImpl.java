package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.PlatformTenantConst;
import com.xiaowei.account.consts.RoleType;
import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.repository.SysRoleRepository;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-21 15:40:33
 * @Description 系统角色服务
 * @Version 1.0
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public SysRoleServiceImpl(@Qualifier("sysRoleRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public SysRole saveRole(SysRole role) {
        //判定参数是否合规
        judgeAttribute(role, JudgeType.INSERT);

        sysRoleRepository.save(role);

        return role;
    }

    /**
     * 判定角色的权限是否超出了父级得到权限,不允许角色添加的权限超出了父级
     *
     * @param parent 父级权限
     * @param child  子级权限
     */
    private void judgePermissions(List<SysPermission> parent, List<SysPermission> child) {
        //如果子级权限为空,则不判定
        if (CollectionUtils.isEmpty(child)) {
            return;
        }
        //如果父级权限为空,则不允许添加
        if (CollectionUtils.isEmpty(parent)) {
            throw new BusinessException("保存失败:该角色不允许添加任何权限");
        }
        child.stream().forEach(sysPermission -> {
            String sysPermissionId = sysPermission.getId();
            if (StringUtils.isEmpty(sysPermissionId)) {
                throw new BusinessException("保存失败:角色的权限没有传入对象id");
            }
            //判定父级权限是否包含该权限
            if (!parent.stream().map(SysPermission::getId).collect(Collectors.toSet()).contains(sysPermissionId)) {
                throw new BusinessException("保存失败:该角色权限有不允许添加的权限");
            }
        });
    }

    private void judgeAttribute(SysRole role, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            role.setId(null);
            //设置code
            role.setCode(StringPYUtils.getSpellCode(role.getName()));
            if (sysRoleRepository.findByCode(role.getCode()) != null) {
                throw new BusinessException("角色编码重复");
            }
            //设置创建时间
            role.setCreatedTime(new Date());

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String roleId = role.getId();

            if (StringUtils.isEmpty(roleId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            Optional<SysRole> byId = sysRoleRepository.findById(roleId);
            EmptyUtils.assertOptional(byId, "保存失败:没有查询到需要修改的对象");
            //只有平台租户才能修改托管角色
            if (!PlatformTenantConst.ID.equals(LoginUserUtils.getLoginUser().getTenancyId())) {
                //判断待修改的角色是否是托管角色
                if (byId.get().getRoleType().equals(RoleType.TRUSTEESHIPROLE.getStatus())) {
                    throw new BusinessException("您无权修改由系统分配的托管角色！");
                }
            }
        }
        //验证所属公司
        judgeCompany(role);


    }

    /**
     * 验证所属公司
     *
     * @param role
     */
    private void judgeCompany(SysRole role) {
        Company company = role.getCompany();
        if (company == null || StringUtils.isEmpty(company.getId())) {//托管角色
            role.setRoleType(RoleType.TRUSTEESHIPROLE.getStatus());
        } else {//公司角色
            EmptyUtils.assertOptional(companyRepository.findById(company.getId()), "没有查询到所属公司");
            role.setRoleType(RoleType.COMPANYROLE.getStatus());
        }

    }


    @Override
    @Transactional
    public SysRole updateRole(SysRole role) {
        judgeAttribute(role, JudgeType.UPDATE);
        sysRoleRepository.save(role);
        return role;
    }

    @Override
    @Transactional
    public void deleteRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Optional<SysRole> optional = sysRoleRepository.findById(roleId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的角色");
        //删除角色判断当前登录用户是否拥有被删除的角色的权限
        if (!LoginUserUtils.hasRoleId(roleId)) {
            throw new UnauthorizedException("保存失败:没有权限删除该角色");
        }
        //若当前用户不是平台租户并且角色属于托管角色，那么就不允许删除此角色
        if (!PlatformTenantConst.ID.equals(LoginUserUtils.getLoginUser().getTenancyId()) && optional.get().getRoleType().equals(RoleType.TRUSTEESHIPROLE.getStatus())) {
            throw new BusinessException("不允许删除由系统分配的托管角色！");
        }
        //删除角色的权限,用户
        sysRoleRepository.deleteUserRoleByRoleId(roleId);
        sysRoleRepository.deleteById(roleId);
    }

}

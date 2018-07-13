package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.RoleType;
import com.xiaowei.account.entity.Department;
import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.repository.DepartmentRepository;
import com.xiaowei.account.repository.SysRoleRepository;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
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
import java.util.UUID;
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
    private DepartmentRepository departmentRepository;

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
            role.setCode(UUID.randomUUID().toString());
            //设置创建时间
            role.setCreatedTime(new Date());

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String roleId = role.getId();

            if (StringUtils.isEmpty(roleId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            SysRole one = sysRoleRepository.getOne(roleId);
            if (one == null) {
                throw new BusinessException("保存失败:没有查询到需要修改的对象");
            }

            //修改角色判断当前登录用户是否拥有被修改的角色的权限
            if (!LoginUserUtils.hasRoleId(roleId)) {
                throw new UnauthorizedException("保存失败:没有权限修改该角色");
            }

        }
        //验证所属部门
        judgeDepartment(role);


    }

    /**
     * 验证所属部门
     *
     * @param role
     */
    private void judgeDepartment(SysRole role) {
        Department department = role.getDepartment();
        if (department == null || StringUtils.isEmpty(department.getId())) {//托管角色
            role.setRoleType(RoleType.TRUSTEESHIPROLE.getStatus());
        } else {//部门角色
            EmptyUtils.assertOptional(departmentRepository.findById(role.getDepartment().getId()),"没有查询到所属部门");
            role.setRoleType(RoleType.DEPARTMENTROLE.getStatus());
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
        SysRole one = sysRoleRepository.getOne(roleId);
        if (one == null) {
            throw new BusinessException("删除失败:没有查询到需要删除的对象");
        }
        //删除角色判断当前登录用户是否拥有被删除的角色的权限
        if (!LoginUserUtils.hasRoleId(roleId)) {
            throw new UnauthorizedException("保存失败:没有权限删除该角色");
        }

        sysRoleRepository.deleteById(roleId);
    }

}

package com.xiaowei.account.service.impl;

import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.repository.SysPermissionRepository;
import com.xiaowei.account.repository.SysRoleRepository;
import com.xiaowei.account.service.ISysPermissionService;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 权限服务
 * @Version 1.0
 */
@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission> implements ISysPermissionService {
    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    public SysPermissionServiceImpl(@Qualifier("sysPermissionRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public SysPermission savePermission(SysPermission permission) {
        //判定参数是否合规
        judgeAttribute(permission, JudgeType.INSERT);
        //规定id为null
        permission.setId(null);
        permission.setCreatedTime(new Date());

        String parentCode = permission.getParentCode();
        //parentCode为空则为第一级
        //如果是其他级  parentCode为父级  code为父级加上ownCode   ownCode为同级最大数加一  11_111
        SysPermission parent = sysPermissionRepository.findByCode(parentCode);
        if (parent == null) {
            throw new BusinessException("保存失败:没有查询到父级");
        }
        //新增权限判断当前登录用户是否拥有其父级
        if(!LoginUserUtils.hasPermissionId(parent.getId())){
            throw new UnauthorizedException("保存失败:没有权限在此处添加权限");
        }
        permission.setLevel(parent.getLevel() + 1);
        setMaxOwnCodeByParentCode(parentCode, permission);
        permission.setCode(parentCode + "_" + permission.getOwnCode());

        sysPermissionRepository.save(permission);
        //新增权限默认拥有父级权限的角色都拥有
        if(!CollectionUtils.isEmpty(parent.getRoles())){
            Set<String> roleIds = parent.getRoles().stream().map(SysRole::getId).collect(Collectors.toSet());
            if(!CollectionUtils.isEmpty(roleIds)){
                roleIds.stream().forEach(s -> {
                    sysRoleRepository.saveRolePermission(s,permission.getId());
                });
            }
        }

        return permission;
    }

    @Override
    @Transactional
    public SysPermission updatePermission(SysPermission permission) {
        judgeAttribute(permission, JudgeType.UPDATE);
        sysPermissionRepository.save(permission);
        return permission;
    }

    @Override
    @Transactional
    public void deletePermission(String permissionId) {
        if (StringUtils.isEmpty(permissionId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Optional<SysPermission> optional = sysPermissionRepository.findById(permissionId);
        SysPermission one = optional.get();
        EmptyUtils.assertOptional(optional,"没有查询到需要删除的角色");
        //删除权限判断当前登录用户是否拥有其被删除的权限
        if(!LoginUserUtils.hasPermissionId(permissionId)){
            throw new UnauthorizedException("保存失败:没有权限删除该权限");
        }

        if (StringUtils.isEmpty(one.getParentCode())) {
            throw new BusinessException("删除失败:不允许删除一级角色");
        }
        sysPermissionRepository.deleteById(permissionId);
        sysPermissionRepository.deleteByCodeLike(one.getCode() + "_%");
    }

    @Override
    public List<String> findByRoleId(String roleId) {
        return sysPermissionRepository.findByRoleId(roleId);
    }

    @Override
    public List<SysPermission> findBySymbolIn(Set<String> symbols) {
        return sysPermissionRepository.findBySymbolIn(symbols);
    }

    private void judgeAttribute(SysPermission permission, JudgeType judgeType) {
        //判定同级下的权限下名称的唯一性
        String name = permission.getName();
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException("保存失败:权限名为空");
        }
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            String parentCode = permission.getParentCode();
            if (StringUtils.isEmpty(parentCode)) {//第一级
                throw new BusinessException("保存失败:不允许添加一级权限");
            }
            //判断symbol是否唯一
            judgeSymbol(permission.getSymbol());
            //判断名称是否唯一
            judgeName(parentCode, name);

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String permissionId = permission.getId();
            if (StringUtils.isEmpty(permissionId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            //查询原数据
            SysPermission one = sysPermissionRepository.getOne(permissionId);
            if (one == null) {
                throw new BusinessException("保存失败:没有查询到需要修改的对象");
            }

            //修改权限判断当前登录用户是否拥有其被修改的权限
            if(!LoginUserUtils.hasPermissionId(permissionId)){
                throw new UnauthorizedException("保存失败:没有权限修改该权限");
            }

            //如果标识修改了  则验证唯一性  否则不发送sql验证
            if (!one.getSymbol().equals(permission.getSymbol())) {
                judgeSymbol(permission.getSymbol());
            }
            if (StringUtils.isEmpty(one.getParentCode())) {//第一级
                throw new BusinessException("保存失败:不允许修改一级权限");
            }
            //如果name修改了 则验证name唯一性  否则不发送sql验证
            if (!one.getName().equals(name)) {
                judgeName(one.getParentCode(), name);
            }
        }
    }

    /**
     * 验证symbol的唯一性
     *
     * @param symbol
     */
    private void judgeSymbol(String symbol) {
        SysPermission bySymbol = sysPermissionRepository.findBySymbol(symbol);
        if (bySymbol != null) {
            throw new BusinessException("保存失败:权限标识重复");
        }
    }

    /**
     * 验证name的唯一性
     *
     * @param parentCode
     * @param name
     */
    private void judgeName(String parentCode, String name) {
        //判断在同级中名称是否唯一
        List<SysPermission> permissions = sysPermissionRepository.findByNameAndParentCode(name, parentCode);
        if (permissions.size() > 0) {
            throw new BusinessException("保存失败:该权限名已被占用");
        }
    }

    /**
     * 给角色设置最大值的ownCode
     *
     * @param parentCode
     * @param permission
     */
    private void setMaxOwnCodeByParentCode(String parentCode, SysPermission permission) {
        Long ownCode = sysPermissionRepository.findMaxOwnCodeByParentCode(parentCode);
        if (ownCode == null) {
            ownCode = 0L;
        }
        permission.setOwnCode(ownCode.intValue() + 1);

    }

    /**
     * 给权限设置code
     *
     * @param permission
     */
    private void setAuthCodeOfPermission(SysPermission permission) {
        String authCode = ((int) (((Math.random() * 9) + 1) * 10000)) + "";
        SysPermission byCode = sysPermissionRepository.findByCode(authCode);
        if (byCode != null) {
            setAuthCodeOfPermission(permission);
        } else {
            permission.setCode(authCode);
        }
    }
}

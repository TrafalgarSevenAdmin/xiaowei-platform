package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.consts.UserStatus;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.repository.SysUserRepository;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountcommon.RoleBean;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.Account;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 系统用户服务
 * @Version 1.0
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements ISysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ISysRoleService sysRoleService;

    public SysUserServiceImpl(@Qualifier("sysUserRepository") BaseRepository repository) {
        super(repository);
    }

    private String generateSalt() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    @Override
    @Transactional
    public SysUser saveUser(SysUser user) {
        //判定参数是否合规
        judgeAttribute(user, JudgeType.INSERT);

        //对密码进行加密
        setPasswordOfUser(user);

        //默认正常状态
        user.setStatus(UserStatus.NORMAL.getStatus());
        //规定id为null
        user.setId(null);
        user.setCreatedTime(new Date());
        sysUserRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public SysUser registerUser(SysUser user) {
        //判定参数是否合规
        judgeAttribute(user, JudgeType.REGISTER);

        //对密码进行加密
        setPasswordOfUser(user);

        //默认正常状态
        user.setStatus(UserStatus.NORMAL.getStatus());
        //规定id为null
        user.setId(null);
        user.setCreatedTime(new Date());
        sysUserRepository.save(user);
        return user;
    }

    @Override
    public SysUser findByLoginName(String loginName) {
        return sysUserRepository.findByLoginName(loginName);
    }

    @Override
    public List<SysUser> findFromCompanys() {
        return sysUserRepository.findFromCompanies();
    }

    @Override
    public SysUser updatePassword(String userId, String oldPassword, String newPassword) {
        SysUser user = sysUserRepository.getOne(userId);
        //判断密码是否正确
        String md5Password = DigestUtils.md5Hex(user.getSalt() + oldPassword);
        if(user.getPassword().equals(md5Password)){
            user.setPassword(newPassword);
            //对密码进行加密
            setPasswordOfUser(user);
        }else{
            throw new BusinessException("旧密码输入错误!");
        }

        return sysUserRepository.save(user);
    }

    @Override
    public Optional<SysUser> findByMobile(String mobile) {
        return sysUserRepository.findByMobile(mobile);
    }

    @Override
    @Transactional
    public SysUser updateUser(SysUser user) {
        judgeAttribute(user, JudgeType.UPDATE);

        sysUserRepository.save(user);
        return user;
    }

    /**
     * 对密码进行加密
     *
     * @param user
     */
    private void setPasswordOfUser(SysUser user) {
        user.setSalt(generateSalt());
        user.setPassword(DigestUtils.md5Hex(user.getSalt() + user.getPassword()));
    }

    /**
     * 伪删除
     *
     * @param userId
     */
    @Override
    @Transactional
    public void fakeDeleteUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Optional<SysUser> sysUser = sysUserRepository.findById(userId);
        EmptyUtils.assertOptional(sysUser, "没有查询到需要删除的对象");
        SysUser one = sysUser.get();
        //admin不允许伪删除
        if (one.getLoginName().equals(SuperUser.ADMINISTRATOR_NAME)) {
            throw new BusinessException("删除失败:不允许删除超级管理员");
        }
        sysUserRepository.delete(one);
    }

    @Override
    @Transactional
    public SysUser updateStatus(SysUser user) {
        String userId = user.getId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException("保存失败:没有传入对象id");
        }
        Optional<SysUser> optional = sysUserRepository.findById(userId);
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        SysUser one = optional.get();
        //admin不允许修改状态
        if (one.getLoginName().equals(SuperUser.ADMINISTRATOR_NAME)) {
            throw new BusinessException("保存失败:不允许修改超级管理员的状态");
        }

        one.setStatus(user.getStatus());
        sysUserRepository.save(one);
        return one;
    }

    private void judgeAttribute(SysUser user, JudgeType judgeType) {
//        //判断所属公司是否为空
//        if (user.getCompany() == null) {
//            throw new BusinessException("保存失败:所属公司为空");
//        }
        //判断loginName是否唯一
        String loginName = user.getLoginName();
        String mobile = user.getMobile();
        if (StringUtils.isEmpty(loginName)) {
            throw new BusinessException("保存失败:用户名为空");
        }
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException("保存失败:电话号码为空");
        }
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            //验证loginName唯一性
            judgeLoginName(loginName);
            //验证电话号码唯一性
            judgeMobile(mobile);
            //判断是否超出了当前登录用户的角色
            if (!CollectionUtils.isEmpty(user.getRoles())) {
                judgeHaveRoles(LoginUserUtils.getLoginUser().getRoles().stream().map(RoleBean::getId).collect(Collectors.toSet()), user.getRoles().stream().map(SysRole::getId).collect(Collectors.toSet()));
            }
            user.setCode(StringPYUtils.getSpellCode("EM"));
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            if (loginName.equals(SuperUser.ADMINISTRATOR_NAME)) {
                throw new BusinessException("保存失败:" + SuperUser.ADMINISTRATOR_NAME + "不允许修改用户名");
            }
            String userId = user.getId();
            if (StringUtils.isEmpty(userId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            //查询原数据
            SysUser one = sysUserRepository.getOne(userId);
            if (one == null) {
                throw new BusinessException("保存失败:没有查询到需要修改的对象");
            }

            //如果loginName修改了  则验证唯一性  否则不发送sql判断
            if (!one.getLoginName().equals(loginName)) {
                //不允许修改admin的用户名
                if (one.getLoginName().equals(SuperUser.ADMINISTRATOR_NAME)) {
                    throw new BusinessException("保存失败:" + SuperUser.ADMINISTRATOR_NAME + "不允许修改用户名");
                }
                judgeLoginName(loginName);
            }

            if (!one.getMobile().equals(mobile)) {
                //验证电话号码唯一性
                judgeMobile(mobile);
            }

            //验证修改用户的角色是否有权限操作
            judgeHaveRoles(user, one.getRoles());

            //设置不允许直接修改的字段
            user.setStatus(one.getStatus());
            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(one.getPassword());
                user.setSalt(one.getSalt());
            } else {
                //对密码进行加密
                setPasswordOfUser(user);
            }
        } else if (judgeType.equals(JudgeType.REGISTER)) {//注册
            //验证loginName唯一性
            judgeLoginName(loginName);
            //验证电话号码唯一性
            judgeMobile(mobile);
            //判断是否超出了当前登录用户的角色
            List<SysRole> registerRoles = sysRoleService.query(new Query().addFilter(Filter.eq("code", AccountConst.REGISTER_ROLE_CODE)));
            if (!CollectionUtils.isEmpty(user.getRoles())) {
                //若分配了角色，则判断分配的角色是否超过限制
                judgeHaveRoles(registerRoles.stream().map(SysRole::getId).collect(Collectors.toSet()), user.getRoles().stream().map(SysRole::getId).collect(Collectors.toSet()));
            } else {
                //若未分配角色，则分配默认的注册角色
                user.setRoles(registerRoles);
            }
            user.setCode(StringPYUtils.getSpellCode("EM"));
        }
    }

    /**
     * 验证修改用户的角色是否有权限操作
     *
     * @param user     被修改的用户
     * @param oldRoles 旧角色集合
     */
    private void judgeHaveRoles(SysUser user, List<SysRole> oldRoles) {
        //修改用户时判断   当前所选角色 > 之前的角色   证明是新勾选的   验证权限并直接添加
        //                 当前所选角色 < 之前的角色   判断是否为当前登录用户的角色   如果是  则证明是去除的   直接去除
        //                                                        如果不是 则证明是当前登录用户无法操作的角色  保持不变

        //确保stream能够执行不报错
        List<SysRole> newRoles = user.getRoles();
        if (oldRoles == null) {
            oldRoles = new ArrayList<>();
        }
        if (newRoles == null) {
            newRoles = new ArrayList<>();
        }

        Set<String> oldRoleIds = oldRoles.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        Set<String> newRoleIds = newRoles.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        //1.旧角色不包含新角色 证明是新增的  验证是否为当前登录用户所能操作
        //2.新角色不包含旧角色 证明是去除的  当前登录用户有则去除 没有重新加上
        newRoleIds.stream().forEach(s -> {
            if (!oldRoleIds.contains(s)) {
                if (!LoginUserUtils.hasRoleId(s)) {
                    throw new UnauthorizedException("保存失败:所选角色权限不足");
                }
            }
        });
        Iterator<SysRole> iterator = oldRoles.iterator();
        while (iterator.hasNext()) {
            SysRole next = iterator.next();
            if (!newRoleIds.contains(next.getId())) {
                if (!LoginUserUtils.hasRoleId(next.getId())) {
                    newRoles.add(next);
                }
            }
        }

    }

    /**
     * 验证新建用户的角色是否超出了当前登录用户的角色
     *
     * @param loginUserRoles 当前登录用户的角色
     * @param roles          操作的角色
     */
    private void judgeHaveRoles(Set<String> loginUserRoles, Set<String> roles) {
        //如果操作角色为空  则不验证
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }
        //如果操作角色不为空  且登录用户角色为空  则抛出异常
        if (CollectionUtils.isEmpty(loginUserRoles)) {
            throw new UnauthorizedException("保存失败:所选角色权限不足");
        }
        roles.stream().forEach(s -> {
            if (!loginUserRoles.contains(s)) {
                throw new UnauthorizedException("保存失败:所选角色权限不足");
            }
        });
    }

    /**
     * 验证loginName唯一性
     *
     * @param loginName
     */
    private void judgeLoginName(String loginName) {
        SysUser user1 = sysUserRepository.findByLoginName(loginName);
        EmptyUtils.assertObjectNotNull(user1, "用户名重复");
    }

    /**
     * 验证电话号码唯一性
     *
     * @param mobile
     */
    private void judgeMobile(String mobile) {
        Optional<SysUser> optional = sysUserRepository.findByMobile(mobile);
        EmptyUtils.assertOptionalNot(optional, "电话号码重复");
    }
}

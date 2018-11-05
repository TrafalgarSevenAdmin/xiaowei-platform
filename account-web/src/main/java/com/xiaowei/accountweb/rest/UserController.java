package com.xiaowei.accountweb.rest;

import com.xiaowei.account.bean.OnlineUser;
import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.query.UserQuery;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.SysUserDTO;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.mq.bean.UserChageMassage;
import com.xiaowei.mq.sender.MessagePushSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xiaowei.accountcommon.LoginUserUtils.SESSION_USER_KEY;

/**
 * 用户管理
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private MessagePushSender messagePushSender;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @RequiresPermissions("account:user:add")
    @ApiOperation(value = "添加用户")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加用户", contentParams = {@ContentParam(useParamField = true, field = "sysUserDTO", value = "用户信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user = sysUserService.saveUser(user);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:update")
    @ApiOperation(value = "修改用户", notes = "只能修改loginName,roles,mobile和email")
    @AutoErrorHandler
    @PutMapping("/{userId}")
    @HandleLog(type = "修改用户", contentParams = {@ContentParam(useParamField = true, field = "sysUserDTO", value = "用户信息"),
            @ContentParam(useParamField = false, field = "userId", value = "用户id")})
    public Result update(@PathVariable("userId") String userId, @RequestBody @Validated(V.Update.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user.setId(userId);
        user = sysUserService.updateUser(user);
        messagePushSender.sendUserInfoChageMessage(new UserChageMassage(userId));
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:status")
    @ApiOperation(value = "启用/禁用")
    @AutoErrorHandler
    @PutMapping("/{userId}/status")
    @HandleLog(type = "启用/禁用用户", contentParams = {@ContentParam(useParamField = false, field = "userId", value = "用户id")})
    public Result updateStatus(@PathVariable("userId") String userId, @RequestBody @Validated(SysUserDTO.UpdateStatus.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = BeanCopyUtils.copy(sysUserDTO, SysUser.class);
        user.setId(userId);
        user = sysUserService.updateStatus(user);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @ApiOperation(value = "修改自己账户的密码")
    @AutoErrorHandler
    @PutMapping("/password")
    @HandleLog(type = "修改自己账户的密码", contentParams = {@ContentParam(useParamField = true, field = "sysUserDTO", value = "用户密码详情")})
    public Result updatePassword(@RequestBody @Validated(SysUserDTO.UpdatePassword.class) SysUserDTO sysUserDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysUser user = sysUserService.updatePassword(LoginUserUtils.getLoginUser().getId(),
                sysUserDTO.getOldPassword(),sysUserDTO.getPassword());
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:delete")
    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    @HandleLog(type = "删除用户", contentParams = {@ContentParam(useParamField = false, field = "userId", value = "用户id")})
    public Result delete(@PathVariable("userId") String userId) {
        //伪删除
        sysUserService.fakeDeleteUser(userId);
        messagePushSender.sendUserInfoChageMessage(new UserChageMassage(userId));
        return Result.getSuccess("删除成功");
    }

    @RequiresPermissions("account:user:get")
    @ApiOperation("根据id获取用户")
    @GetMapping("/{userId}")
//    @HandleLog(type = "根据id获取用户",contentParams = {@ContentParam(field = "userId",value = "用户id")})
    public Result findById(@PathVariable("userId") String userId, FieldsView fieldsView) {
        SysUser user = sysUserService.findById(userId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(user, fieldsView));
    }

    @RequiresPermissions("account:user:query")
    @ApiOperation("用户查询接口")
    @GetMapping("")
    public Result query(UserQuery userQuery, FieldsView fieldsView) {
        //设置默认条件
        setDefaultCondition(userQuery);
        if (userQuery.isNoPage()) {
            List<SysUser> users = sysUserService.query(userQuery,SysUser.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(users, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = sysUserService.queryPage(userQuery,SysUser.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(UserQuery userQuery) {
        //默认只能查询当前登录用户所在公司下的用户
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if(!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())){
            userQuery.setCompanyId(loginUser.getCompanyBean().getId());
        }
    }

    @RequiresPermissions("account:user:get:online")
    @ApiOperation("获取当前在线用户")
    @GetMapping("/online")
    public Result online(FieldsView fieldsView) {
        Set<String> onlineUserKeys = redisTemplate.opsForSet().members(AccountConst.ON_LINE_USER_KEY);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String id : onlineUserKeys) {
            Session session = (Session) redisTemplate.opsForValue().get(AccountConst.USER_REDIS_GROUP_PREFIX + id);
            LoginUserBean user;
            if (session != null && (user = (LoginUserBean) session.getAttribute(SESSION_USER_KEY)) != null) {
                if (LoginUserUtils.getLoginUserOrNull().getTenancyId().equals(user.getTenancyId())) {
                    onlineUsers.add(new OnlineUser(session, user));
                }
            }
        }
        //根据最后一次请求排序
        onlineUsers = onlineUsers.stream().sorted((a, b) -> (int)(b.getLastAccessTime().getTime() - a.getLastAccessTime().getTime())).collect(Collectors.toList());
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(onlineUsers, fieldsView));
    }

    @RequiresPermissions("account:user:delete:online")
    @ApiOperation("移除掉在线用户")
    @DeleteMapping("/online/{id}")
    public Result logout(@PathVariable("id") String sessionId) {
        redisTemplate.opsForSet().remove(AccountConst.ON_LINE_USER_KEY, Arrays.asList(sessionId));
        redisTemplate.opsForValue().getOperations().delete(AccountConst.USER_REDIS_GROUP_PREFIX + sessionId);
        return Result.getSuccess();
    }

}

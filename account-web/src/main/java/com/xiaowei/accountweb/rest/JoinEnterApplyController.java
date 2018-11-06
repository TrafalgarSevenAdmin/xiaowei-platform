package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.PlatformTenantConst;
import com.xiaowei.account.entity.AuditConfiguration;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.multi.MultiTenancyPostProcessor;
import com.xiaowei.account.service.IAuditConfigurationService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.status.AuditTypeStatus;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.JoinAuditDto;
import com.xiaowei.accountweb.dto.JoinEnterDto;
import com.xiaowei.accountweb.dto.JoinEnterQueryDto;
import com.xiaowei.accountweb.entity.JoinEnterApply;
import com.xiaowei.accountweb.service.IJoinEnterApplyService;
import com.xiaowei.accountweb.service.IVerificationCodeService;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MessageType;
import com.xiaowei.mq.sender.MessagePushSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xiaowei.accountcommon.LoginUserUtils.SESSION_USER_KEY;

/**
 * 加盟入驻接口
 */
@Slf4j
@Api(tags = "加盟入驻接口")
@RestController
@RequestMapping("/api/join")
public class JoinEnterApplyController {

    @Autowired
    private IJoinEnterApplyService joinEnterApplyService;

    @Autowired
    IVerificationCodeService verificationCodeService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IAuditConfigurationService auditConfigurationService;

    @Autowired
    private MessagePushSender messagePushSender;


    @ApiOperation(value = "获取验证码")
    @AutoErrorHandler
    @GetMapping("/code")
    public Result getCode(String email, String phone) {
        return Result.getSuccess(verificationCodeService.sendCode(email, phone));
    }
    @ApiOperation(value = "验证验证码")
    @AutoErrorHandler
    @PutMapping("/verification/code")
    public Result getCode(String code) {
        verificationCodeService.verificationCode(code);
        return Result.getSuccess();
    }

    @ApiOperation(value = "入驻申请单保存")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "入驻申请单保存", contentParams = {@ContentParam(useParamField = true, field = "joinEnterDto", value = "入驻申请单")})
    public Result insert(@RequestBody @Validated(V.Insert.class) JoinEnterDto joinEnterDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        JoinEnterApply joinEnterApply = BeanCopyUtils.copy(joinEnterDto, JoinEnterApply.class);
        if (StringUtils.isNotEmpty(joinEnterDto.getTenementId())) {
            joinEnterApply.setTenancyId(joinEnterDto.getTenementId());
        } else {
            //作为公司入驻，设置为平台用户审核。
            joinEnterApply.setTenancyId(PlatformTenantConst.ID);
        }
        if (LoginUserUtils.isLogin()) {
            //若此申请通过微信页面填写，并获得了该用户的openId,此时只需要将当前的登录用户记下来，方便查询自己的申请记录状态以及分配用户时自动绑定微信号
            joinEnterApply.setOpenId((String) SecurityUtils.getSubject().getSession().getAttribute("openId"));
        }
        joinEnterApplyService.save(joinEnterApply);

        //更新当前登陆用户的租户，方便下一步查询审核人员时，自动带上租户
        LoginUserBean loginUserOrNull = LoginUserUtils.getLoginUserOrNull();
        loginUserOrNull.setTenancyId(joinEnterDto.getTenementId());
        SecurityUtils.getSubject().getSession().setAttribute(SESSION_USER_KEY,loginUserOrNull);

        messageToSendorder(joinEnterApply);
        return Result.getSuccess();
    }

    @RequiresPermissions("account:join:query")
    @ApiOperation("入驻申请查询查询接口")
    @GetMapping("")
    public Result query(JoinEnterQueryDto query, FieldsView fieldsView) {
        //查询公司设置默认条件
        if (query.isNoPage()) {
            List<JoinEnterApply> joinEnterApplies = joinEnterApplyService.query(query, JoinEnterApply.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(joinEnterApplies, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = joinEnterApplyService.queryPage(query, JoinEnterApply.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    @ApiOperation(value = "获取我的申请记录", notes = "在获取之前必须使用微信登陆一次，才能够获取到当前的登陆的用户openId")
    @GetMapping("/me")
    public Result getMeJoinApply() {
        LoginUserUtils.getLoginUser();
        String openId = (String) SecurityUtils.getSubject().getSession().getAttribute("openId");
        if (StringUtils.isNotEmpty(openId)) {
            //查询时不筛选登陆用户的租户，毕竟登陆用户可能是访客用户，并没有租户
            MultiTenancyPostProcessor.SecurecyAdvice.tempChageTenancyId.set(MultiTenancyPostProcessor.SecurecyAdvice.notFilterTenancy);
            List<JoinEnterApply> joinEnterApplies = joinEnterApplyService.query(new Query().addFilter(Filter.eq("openId", openId)));
            if (CollectionUtils.isNotEmpty(joinEnterApplies)) {
                return Result.getSuccess(joinEnterApplies.get(0));
            }
        }
        return Result.getSuccess();
    }

    @RequiresPermissions("account:join:audit")
    @ApiOperation(value = "入驻申请审核接口", notes = "非平台租户中的用户不能审核公司入驻的申请，只能审核此租户下的加盟工程师申请")
    @PostMapping("/audit")
    @AutoErrorHandler
    @HandleLog(type = "审核申请单", contentParams = {@ContentParam(useParamField = true, field = "joinAuditDto", value = "审核申请单请求")})
    public Result auditPass(@RequestBody @Validated(V.Insert.class) JoinAuditDto joinAuditDto,BindingResult bindingResult, FieldsView fieldsView) {
        joinEnterApplyService.audit(joinAuditDto);
        return Result.getSuccess();
    }

    @RequiresPermissions("account:join:delete")
    @ApiOperation("删除申请单")
    @DeleteMapping("/{id}")
    @HandleLog(type = "删除申请单", contentParams = {@ContentParam(field = "id", value = "申请单id")})
    public Result delete(@PathVariable("id") String id) {
        joinEnterApplyService.delete(id);
        return Result.getSuccess();
    }

    /**
     * 需要派单员派单的通知
     * @param joinEnterApply
     */
    private void messageToSendorder(JoinEnterApply joinEnterApply) {
        //查询派单员
        Set<String> userIds = auditConfigurationService.findByType(AuditTypeStatus.AUDITTENEMENTID.getStatus()).stream().map(AuditConfiguration::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<SysUser> users = userService.findByUserIdIn(userIds);
        for (SysUser user : users) {
            try {
                UserMessageBean userMessageBean = new UserMessageBean();
                userMessageBean.setUserId(user.getId());
                userMessageBean.setMessageType(MessageType.MESSAGETOSENDORDER);
                Map<String, UserMessageBean.Payload> messageMap = new HashMap<>();
                messageMap.put("first", new UserMessageBean.Payload("贵公司有新的加盟申请,请尽快登陆PC端后台处理", null));
                messageMap.put("keyword1", new UserMessageBean.Payload(user.getCompany() != null ? user.getCompany().getCompanyName() : null, null));
                messageMap.put("keyword2", new UserMessageBean.Payload(joinEnterApply.getUserName(), null));
                messageMap.put("keyword3", new UserMessageBean.Payload(joinEnterApply.getMobilePhone(), null));
                userMessageBean.setData(messageMap);
                messagePushSender.sendWxMessage(userMessageBean);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                e.printStackTrace();
            }
        }

    }

}

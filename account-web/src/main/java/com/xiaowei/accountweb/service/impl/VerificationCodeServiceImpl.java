package com.xiaowei.accountweb.service.impl;

import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.utils.ConfigUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.VerificationCodeRecord;
import com.xiaowei.accountweb.service.IVerificationCodeService;
import com.xiaowei.core.exception.BusinessException;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log
@Service
public class VerificationCodeServiceImpl implements IVerificationCodeService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, VerificationCodeRecord> redisTemplate;

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String form;


    private static final String verificationCodeIdKey = "VerificationCodeId";

    /**
     * 验证邮件的标题
     */
    private static final String VERIFICATION_MAIL_TITLE_KEY = "verification.mail.title";

    /**
     * 验证邮件的内容，使用${code}填充验证码
     */
    private static final String VERIFICATION_MAIL_COTEXT_KEY = "verification.mail.context";
    /**
     * 发送验证码
     * @param email
     * @param phone
     * @return
     */
    @Override
    public Boolean sendCode(String email,String phone) {
        //需要用户先登陆，登陆为访客用户
        LoginUserUtils.getLoginUser();
        Object verificationCodeId = SecurityUtils.getSubject().getSession().getAttribute(verificationCodeIdKey);
        //判断一下一分钟前是否发送了验证码
        if (verificationCodeId != null) {
            VerificationCodeRecord verificationCodeRecord = redisTemplate.opsForValue().get(AccountConst.VERIFICATION_CODE_REDIS_GROUP_PREFIX + verificationCodeId);
            //规定一分钟只能发送一次，因此前端要做展示限定，并且再一分钟内申请的验证码发送请求一律给予成功反馈，但实际上并不发送
            if (verificationCodeRecord != null && DateUtils.addMinutes(verificationCodeRecord.getTime(), 1).after(new Date())) {
                return true;
            } else {
                //因为后面要重新刷新验证码，因此先删掉此验证码再说
                redisTemplate.delete(AccountConst.VERIFICATION_CODE_REDIS_GROUP_PREFIX + verificationCodeId);
            }
        }
        verificationCodeId = UUID.randomUUID().toString();
        //产生6位数验证码
        String code = String.valueOf(RandomUtils.nextLong(100000, 999999));
        //发送验证码
        sendEmailCode(email, code);
        sendPhoneCode(phone, code);
        VerificationCodeRecord verificationCodeRecord = VerificationCodeRecord.builder().code(code).email(email).phone(phone).time(new Date()).build();
        //写入到redis以及当前登录用户（访客）的上下文中
        redisTemplate.opsForValue().set(AccountConst.VERIFICATION_CODE_REDIS_GROUP_PREFIX + verificationCodeId, verificationCodeRecord);
        redisTemplate.expire(AccountConst.VERIFICATION_CODE_REDIS_GROUP_PREFIX + verificationCodeId, 10, TimeUnit.MINUTES);//10分钟后过期
        SecurityUtils.getSubject().getSession().setAttribute(verificationCodeIdKey, verificationCodeId);
        return true;
    }

    /**
     * 发送邮件验证码
     * @param to
     * @param code
     */
    @Override
    public void sendEmailCode(String to, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(form);
            helper.setTo(to);
            //主题
            helper.setSubject(ConfigUtils.getConfigValueOrDefault(VERIFICATION_MAIL_TITLE_KEY,"晓维快修-验证码"));
            //内容
            helper.setText(ConfigUtils.getConfigValueOrDefault(VERIFICATION_MAIL_COTEXT_KEY,"【晓维快修】您的验证码是<span style=\"color: red\">${code}</span>，在10分钟内有效。如非本人操作请忽略本邮件")
                    .replace("${code}",code), true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BusinessException("邮件验证码发送失败！",e);
        }
    }

    /**
     * 发送手机验证码
     * @param to
     * @param code
     */
    @Override
    public void sendPhoneCode(String to, String code) {
        log.warning("发送手机验证码功能暂未实现！");
    }

    /**
     * 验证
     * @param code
     * @return
     */
    @Override
    public void verificationCode(String code) {
        LoginUserUtils.getLoginUser();
        Object verificationCodeId = SecurityUtils.getSubject().getSession().getAttribute(verificationCodeIdKey);
        //判断一下一分钟前是否发送了验证码
        if (verificationCodeId != null) {
            VerificationCodeRecord verificationCodeRecord = redisTemplate.opsForValue().get(AccountConst.VERIFICATION_CODE_REDIS_GROUP_PREFIX + verificationCodeId);
            if (verificationCodeRecord == null) {
                throw new BusinessException("验证码已过期，请重新发送!");
            }
            if (!verificationCodeRecord.getCode().equals(code)) {
                throw new BusinessException("验证码错误!");
            }
        } else {
            throw new BusinessException("请先发送验证码！");
        }
    }
}

package com.xiaowei.accountweb.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码记录，存放于redis中
 */
@Data
@Builder
public class VerificationCodeRecord implements Serializable {
    /**
     * 验证的邮箱号
     */
    String email;

    /**
     * 验证的手机号
     */
    String phone;

    /**
     * 时间
     */
    Date time;

    /**
     * 验证码
     */
    String code;
}

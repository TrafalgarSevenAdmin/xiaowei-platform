package com.xiaowei.accountweb.service;

public interface IVerificationCodeService {
    Boolean sendCode(String email, String phone);

    void sendEmailCode(String to, String code);

    void sendPhoneCode(String to, String code);

    void verificationCode(String code);
}

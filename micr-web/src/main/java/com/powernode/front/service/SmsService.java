package com.powernode.front.service;

public interface SmsService {
    /**
     * @param phone 手机号
     * @return true: 发送成功
     */
    //发送短信
    boolean sendSms(String phone);

    /**
     * @param phone 手机号
     * @param code 提交参数的验证码
     * @return
     */
    boolean checkSmsCode(String phone, String code);
}

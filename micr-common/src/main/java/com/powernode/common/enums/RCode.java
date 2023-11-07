package com.powernode.common.enums;

public enum RCode {

    UNKNOWN(0, "请稍后重试"),
    SUCC(1000,"请求成功"),
    REQUEST_PARAM_ERR(1001, "请求参数有误"),
    REQUEST_PRODUCT_TYPE_ERR(1002, "产品类型有误"),
    PRODUCT_OFFLINE(1003,"产品已经下架"),
    PHONE_FORMAT_ERR(1004,"手机号格式不正确"),
    PHONE_EXISTS(1005, "手机号已存在"),
    SMS_CODE_CAN_USE(1006, "验证码可以继续使用"),
    SMS_CODE_INVALID(1007, "验证码有误"),
    PHONE_LOGIN_PASSWORD_INVALID(1008,"手机号或密码无效"),
    REALNAME_FAIL(1009, "实名认证失败"),
    REALNAME_RETRY(1010, "已经实名认证过了"),
    TOKEN_INVALID(3000, "token无效");

    // 应答码 1000 - 2000 参数有误，逻辑问题
    // 2000- 3000 是服务器请求错误
    // 3000- 4000 访问dubbo的应答结果
    private int code;
    private String text;

    private RCode(int c, String t) {
        this.code = c;
        this.text = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

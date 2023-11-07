package com.powernode.front.controller;

import com.powernode.common.constants.RedisKey;
import com.powernode.common.enums.RCode;
import com.powernode.common.util.CommonUtil;
import com.powernode.front.service.SmsService;
import com.powernode.front.view.RespResult;
import io.swagger.annotations.Api;
import org.springframework.data.redis.connection.ReactivePubSubCommands;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1/sms")
public class SmsController extends BaseController {

    @Resource(name = "smsCodeRegisterImpl")
    private SmsService smsService;

    @Resource(name = "smsCodeLoginImpl")
    private SmsService loginSmsService;

    // 发送短信验证码
    @GetMapping("/code/register")
    public RespResult sendCodeRegister(@RequestParam String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            // 判断redis中是否有这个手机号的验证码
            String key = RedisKey.KEY_SMS_CODE_REG +phone;
            if (stringRedisTemplate.hasKey(key)) {
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            } else {
                boolean isSuccess = smsService.sendSms(phone);
                if (isSuccess) {
                    result = RespResult.ok();
                }
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }



    // 发送短信登录验证码
    @GetMapping("/code/login")
    public RespResult sendCodeLogin(@RequestParam String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            // 判断redis中是否有这个手机号的验证码
            String key = RedisKey.KEY_SMS_CODE_LOGIN +phone;
            if (stringRedisTemplate.hasKey(key)) {
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            } else {
                boolean isSuccess = loginSmsService.sendSms(phone);
                if (isSuccess) {
                    result = RespResult.ok();
                }
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

}

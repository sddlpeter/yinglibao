package com.powernode.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.powernode.common.constants.RedisKey;
import com.powernode.front.config.JdwxSmsConfig;
import com.powernode.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 登录发送短信验证码
 */

@Service(value = "smsCodeLoginImpl")
public class SmsCodeLoginImpl implements SmsService {

    @Resource
    private JdwxSmsConfig smsConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendSms(String phone) {
        boolean send = false;
        String path = smsConfig.getUrl();
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.setCredentials(smsConfig.getAppkey(), smsConfig.getAppsecret());
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("=========== 登录验证码随机数 random=" + random + " ============");

        String content = String.format(smsConfig.getLoginText(), random);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
        request.addQueryParameter("content", content);
        request.addQueryParameter("mobile", phone);

        // 设置jsonBody参数
        String jsonBody = "{\"code\":\"success\"}";
        request.setJsonBody(jsonBody);

        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            System.out.println(response.getResult());
            JSONObject jsonObject = JSONObject.parseObject(response.getResult());
            if (jsonObject.getString("ReturnStatus").equals("Success")) {
                send = true;

                // 保存短信验证码到redis
                String key = RedisKey.KEY_SMS_CODE_LOGIN + phone;
                // ------------- 这里先写死，因为用的是免费试用短信api，所以必须发送的是5873的验证码，将来买完之后，再设置为random
                stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKey.KEY_SMS_CODE_LOGIN + phone;
        if (stringRedisTemplate.hasKey(key)) {
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)) {
                return true;
            }
        }
        return false;
    }
}

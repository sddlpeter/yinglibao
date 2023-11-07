package com.powernode.front.service;


import com.alibaba.fastjson.JSONObject;
import com.powernode.api.service.UserService;
import com.powernode.front.config.JdwxRealnameConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service(value = "realnameServiceImpl")
public class RealnameServiceImpl {

    @Resource
    private JdwxRealnameConfig realnameConfig;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    private UserService userService;


    // true: 认证通过
    public boolean handleRealname(String phone, String name, String idCard) {

        boolean realname = false;

        String path = realnameConfig.getUrl();
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.setCredentials(realnameConfig.getAppkey(), realnameConfig.getAppsecret());

        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
//        Map<String, String> params = new HashMap<>();
//        params.put("idcard", idCard);
//        params.put("name",name);

        request.addQueryParameter("name", name);
        request.addQueryParameter("idcard", idCard);

        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            String resp = response.toString();
            if (StringUtils.isNotBlank(resp)) {
                JSONObject respObject = JSONObject.parseObject(resp);
                if ("200".equalsIgnoreCase(respObject.getString("code"))) {
                    // 解析result
                    realname = respObject.getJSONObject("data").getBooleanValue("result");

                    // 处理更新数据库
                    boolean modifyResult = userService.modifyRealname(phone, name, idCard);
                    realname = modifyResult;
                }
            }
            // 返回结果格式为Json字符串
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return realname;
    }
}

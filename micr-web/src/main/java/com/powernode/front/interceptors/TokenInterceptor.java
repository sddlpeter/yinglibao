package com.powernode.front.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.powernode.common.enums.RCode;
import com.powernode.common.util.JwtUtil;
import com.powernode.front.view.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    private String secret = "";

    public TokenInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 如果是option请求，预处理请求，要放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        boolean requestSend = false;
        try {
            // 2. 需要获取token的，进行验证
            String headerUid = request.getHeader("uid");
            String headerToken = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(headerToken)) {
                // Bearer xxxx....
                String jwt = headerToken.substring(7);
                // 读jwt
                JwtUtil jwtUtil = new JwtUtil(secret);
                Claims claims = jwtUtil.readJwt(jwt);

                Integer jwtUid = claims.get("uid", Integer.class);
                if (headerUid.equals(String.valueOf(jwtUid))) {
                    // token和发起请求用户是同一个，请求可以被处理
                    requestSend = true;
                }
            }
        } catch (Exception e) {
            requestSend = false;
            e.printStackTrace();
        }

        // token 没有验证通过，需要给vue错误提示
        if (requestSend == false) {
            RespResult result = RespResult.fail();
            result.setRCode(RCode.TOKEN_INVALID);

            // 试用httpServletResponse 输出json
            String respJson = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(respJson);
            out.flush();
            out.close();
        }

        return requestSend;
    }
}

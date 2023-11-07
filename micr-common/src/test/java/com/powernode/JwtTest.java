package com.powernode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTest {

    // 创建jwt
    @Test
    public void testCreateJwt() {
        // String s = UUID.randomUUID().toString();
        // System.out.println(s.replaceAll("-", ""));
        String key = "8339abdf4e694b35bc2de7044a9a8077";

        // 创建secretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        Date curDate = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1001);
        data.put("name", "lisi");
        data.put("role", "manager");

        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(new Date(), 10))
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .addClaims(data).compact();

        System.out.println("jwt="+jwt);
    }

    @Test
    public void testReadJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTYwOTk0ODUsImlhdCI6MTY5NjA5ODg4NiwianRpIjoiYWM3ZWJlNDktNWRhYi00NTY4LWJiYTQtNjZiNWRkODQ5NDU2Iiwicm9sZSI6Im1hbmFnZXIiLCJuYW1lIjoibGlzaSIsInVzZXJJZCI6MTAwMX0.XHUiEW2QDMf9dQF8AZCbp6tOEXbaVeX4FVlhz5W3zwE";
        String key = "8339abdf4e694b35bc2de7044a9a8077";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        // 解析jwt，没有异常，解析成功
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt);

        // 读数据
        Claims body = claims.getBody();
        Integer userId = body.get("userId", Integer.class);
        System.out.println("userId=" + userId);
        Object uid = body.get("userId");
        System.out.println("uid=" + uid);

        Object name = body.get("name");
        if (name != null) {
            String str = (String) name;
            System.out.println("str=" + str);
        }

        String jwtId = body.getId();
        System.out.println("jwtId=" +jwtId);

        Date expiration = body.getExpiration();
        System.out.println("expiration=" + expiration);

    }
}

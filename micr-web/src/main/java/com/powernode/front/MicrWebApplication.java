package com.powernode.front;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.powernode.common.util.JwtUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


// 启用swagger 和ui
@EnableSwaggerBootstrapUI
@EnableSwagger2

// 启用dubbo服务
@EnableDubbo
@SpringBootApplication
public class MicrWebApplication {

    @Value("${jwt.secret}")
    private String secretKey;
    // 创建jwtUtil对象
    @Bean
    public JwtUtil jwtUtil() {
        JwtUtil jwtUtil = new JwtUtil(secretKey);
        return jwtUtil;
    }

	public static void main(String[] args) {
		SpringApplication.run(MicrWebApplication.class, args);
	}

}

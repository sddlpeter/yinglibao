package com.powernode.front.controller;

import com.powernode.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class BaseController {

    // 声明公共方法，属性等

    // 声明redis
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    // 平台信息服务
    @DubboReference(interfaceClass = PlatBaseInfoService.class, version = "1.0")
    protected PlatBaseInfoService platBaseInfoService;

    // 产品服务
    @DubboReference(interfaceClass = ProductService.class, version = "1.0")
    protected ProductService productService;

    // 投资服务
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = RechargeService.class, version = "1.0")
    protected RechargeService rechargeService;
}

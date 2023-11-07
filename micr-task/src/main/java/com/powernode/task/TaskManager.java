package com.powernode.task;

import com.powernode.api.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskManager {

    // 定义放法, 1. 方法没有参数， 2. 方法没有返回值

//    @Scheduled(cron = "*/5 * * * * ?")
//    public void testCron() {
//        System.out.println("Cron表达式执行了..." + new Date());
//    }

    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    private IncomeService incomeService;

    // 生成收益计划
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan() {
        incomeService.generateIncomePlan();
    }

    // 生成收益返还
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomeBack() {
        incomeService.generateIncomeBack();
    }

    // 补单接口
    @Scheduled(cron = "0 0/20 * * * ?")
    public void invokeKuaiQianQuery() {
        try{
            String url = "http://localhost:9000/pay/kq/rece/query";
            // HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

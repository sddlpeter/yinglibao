package com.powernode.pay.controller;

import com.powernode.api.model.User;
import com.powernode.service.KuaiQianService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQianController {

    @Resource
    private KuaiQianService KQService;

    // 接收来自vue项目的支付充值请求
    @GetMapping("/rece/recharge")
    public String receFrontRechargeKQ(Integer uid, BigDecimal rechargeMoney, Model model) {
        // 默认是错误
        String view = "err";

        System.out.println(uid + " " + rechargeMoney);

        if (uid != null && uid > 0 && rechargeMoney != null && rechargeMoney.doubleValue() > 0) {
            try {
                //1. 检查uid是否是有效的用户
                User user = KQService.queryUser(uid);
                if (user != null) {
                    Map<String, String> data = KQService.generateFormData(uid, user.getPhone(), rechargeMoney);
                    model.addAllAttributes(data);

                    // 创建充值记录
                    KQService.addRecharge(uid, rechargeMoney, data.get("orderId"));
                    // 把订单号存到redis
                    KQService.addOrderIdToRedis(data.get("orderId"));

                    // 提交支付请求给块钱的form页面 (thymeleaf)
                    view = "kqForm";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    // 接收块钱给商家的支付结果, 快钱以get方式，返送请求给商家
    @GetMapping("/rece/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request) {
        KQService.kqNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    // 从定时任务，调用的接口
    @GetMapping("/rece/query")
    @ResponseBody
    public String queryKQOrder() {
        KQService.handleQueryOrder();
        return "接收查询的请求";
    }
}

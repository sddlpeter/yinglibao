package com.powernode.api.service;

import com.powernode.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {
    // 根据userId查询他的充值记录
    List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize);

    int addRechargeRecord(RechargeRecord record);

    // 处理后续通知
    int handleKQNotify(String orderId, String payAmount, String payResult);
}

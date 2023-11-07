package com.powernode.dataservice.service;

import com.powernode.api.model.RechargeRecord;
import com.powernode.api.service.RechargeService;
import com.powernode.common.constants.YLBConstant;
import com.powernode.common.util.CommonUtil;
import com.powernode.dataservice.mapper.FinanceAccountMapper;
import com.powernode.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class, version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeMapper;

    @Resource
    private FinanceAccountMapper accountMapper;

    // 根据userId 查询他的重置记录
    @Override
    public List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();

        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = rechargeMapper.selectByUid(uid, offset, pageSize);
        }

        return records;
    }

    @Override
    public int addRechargeRecord(RechargeRecord record) {
        return rechargeMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0; //订单不存在
        int rows = 0;
        // 1. 查询订单
        RechargeRecord record = rechargeMapper.selectByRechargeNo(orderId);
        if (record != null) {
            if (record.getRechargeStatus() == YLBConstant.RECHARGE_STATUS_PROCESSING){
                // 2. 判断金额
                String fen = record.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if (fen.equals(payAmount)) {
                    // 金额一致
                    if ("10".equals(payResult)) {
                        // 成功
                        rows = accountMapper.updateAvailableMoneyByRecharge(record.getUid(), record.getRechargeMoney());
                        if (rows < 1) {
                            throw new RuntimeException("充值更新资金账户失败");
                        }
                        rows = rechargeMapper.updateStatus(record.getId(), YLBConstant.RECHARGE_STATUS_SUCCESS);
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 1;
                    } else {
                        // 失败
                        rows = rechargeMapper.updateStatus(record.getId(), YLBConstant.RECHARGE_STATUS_FAIL);
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 2;
                    }
                } else {
                    // 金额不一致
                }

            } else {
                result = 3; // 订单已经处理
            }
        }

        return result;

    }
}

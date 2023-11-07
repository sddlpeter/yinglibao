package com.powernode.front.view.recharge;

import com.powernode.api.model.RechargeRecord;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

public class ResultView {
    private Integer id;
    private String result = "未知";
    private String rechargeDate = "-";
    private BigDecimal rechargeMoney;

    public ResultView(RechargeRecord record) {
        this.id = record.getId();
        this.rechargeMoney = record.getRechargeMoney();

        if (record.getRechargeTime() != null) {
            rechargeDate = DateFormatUtils.format(record.getRechargeTime(), "yyyy-MM-dd");
        }
        switch (record.getRechargeStatus()) {
            case 0:
                result = "充值中";
            case 1:
                result = "成功";
            case 2:
                result = "失败";
        }
    }

    public ResultView(Integer id, String result, String rechargeDate, BigDecimal rechargeMoney) {
        this.id = id;
        this.result = result;
        this.rechargeDate = rechargeDate;
        this.rechargeMoney = rechargeMoney;
    }

    public Integer getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

}

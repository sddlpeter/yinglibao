package com.powernode.dataservice.mapper;

import com.powernode.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    // 给uid的记录上锁
    FinanceAccount selectByUidForUpdate(@Param("uid") Integer uid);

    //更新资金
    int updateAvailableMoneyByInvest(@Param("uid") Integer uid, @Param("money") BigDecimal money);

    // 投资收益返还，更新资金
    int updateAvailableMoneyByIncomeBack(@Param("uid") Integer uid,
                                         @Param("bidMoney") BigDecimal bidMoney,
                                         @Param("incomeMoney") BigDecimal incomeMoney);

    // 充值更新金额
    int updateAvailableMoneyByRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);
}
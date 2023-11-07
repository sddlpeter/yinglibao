package com.powernode.dataservice.service;

import com.powernode.api.model.BidInfo;
import com.powernode.api.model.FinanceAccount;
import com.powernode.api.model.IncomeRecord;
import com.powernode.api.model.ProductInfo;
import com.powernode.api.service.IncomeService;
import com.powernode.common.constants.YLBConstant;
import com.powernode.dataservice.mapper.BidInfoMapper;
import com.powernode.dataservice.mapper.FinanceAccountMapper;
import com.powernode.dataservice.mapper.IncomeRecordMapper;
import com.powernode.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class, version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private IncomeRecordMapper incomeMapper;

    @Resource
    private FinanceAccountMapper accountMapper;

    // 收益计划
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomePlan() {


        // 1. 获取要处理的理财产品信息

        Date currDate = new Date();
        Date beginTime = DateUtils.truncate(DateUtils.addDays(currDate, -1), Calendar.DATE);;
        Date endTime = DateUtils.truncate(currDate, Calendar.DATE);;
        List<ProductInfo> productInfoList = productInfoMapper.selectFullTimeProducts(beginTime, endTime);


        // 2. 查询每个理财产品的多个投资记录
        BigDecimal income = null;
        BigDecimal dayRate = null;
        BigDecimal cycle = null;
        Date incomeDate = null; // 到期时间

        int rows = 0;

        for (ProductInfo product : productInfoList) {
            dayRate = product.getRate().divide(new BigDecimal("360"), 10, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

            // 产品类型不同，周期不同
            if (product.getProductType() == YLBConstant.PRODUCT_TYPE_XINSHOUBAO) {
                cycle = new BigDecimal(product.getCycle());
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle()));
            } else {
                cycle = new BigDecimal(product.getCycle() * 30);
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle() * 30));
            }

            List<BidInfo> bidList = bidInfoMapper.selectByProdId(product.getId());
            // 3. 计算每笔投资的利息和到期时间
            for (BidInfo bid : bidList) {
                // 利息 = 本金 * 周期 * 利率
                income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
                // 创建收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bid.getId());
                incomeRecord.setBidMoney(bid.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeStatus(YLBConstant.INCOME_STATUS_PLAN);
                incomeRecord.setProdId(product.getId());
                incomeRecord.setIncomeMoney(income);
                incomeRecord.setUid(bid.getUid());

                incomeMapper.insertSelective(incomeRecord);

            }


            // 更新产品的状态
            rows = productInfoMapper.updateStatus(product.getId(), YLBConstant.INCOME_STATUS_PLAN);

            if (rows <1) {
                throw new RuntimeException("生成收益计划，更新产品状态失败");
            }
        }
    }

    // 收益返还
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateIncomeBack() {
        //1. 处理到期的收益记录
        Date curDate = new Date();
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(curDate, -1), Calendar.DATE);
        System.out.println(expiredDate);
        List<IncomeRecord> incomeRecordList = incomeMapper.selectExpiredIncome(expiredDate);

        // 2. 把每个收益，进行返还，本金 + 利息
        for (IncomeRecord ir : incomeRecordList) {
            int rows = accountMapper.updateAvailableMoneyByIncomeBack(ir.getUid(), ir.getBidMoney(), ir.getIncomeMoney());
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新账号资金失败");
            }

            //3. 更新收益资金账号状态为1
            ir.setIncomeStatus(YLBConstant.INCOME_STATUS_BACK);
            rows = incomeMapper.updateByPrimaryKey(ir);
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新收益记录的状态失败");
            }

        }
    }
}

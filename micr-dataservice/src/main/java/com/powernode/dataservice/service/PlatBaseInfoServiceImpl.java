package com.powernode.dataservice.service;

import com.powernode.api.pojo.BaseInfo;
import com.powernode.api.service.PlatBaseInfoService;
import com.powernode.dataservice.mapper.BidInfoMapper;
import com.powernode.dataservice.mapper.ProductInfoMapper;
import com.powernode.dataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;


@DubboService(interfaceClass = PlatBaseInfoService.class, version = "1.0")
public class PlatBaseInfoServiceImpl implements PlatBaseInfoService {

    // 注入mapper
    @Resource
    private UserMapper userMapper;

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    // 查询平台基本信息
    @Override
    public BaseInfo queryPlatBaseInfo() {
        // 获取注册人数，获取收益率平均值，累计成交金额
        int registerUser = userMapper.selectCountUsers();

        // 收益率平均值
        BigDecimal avgRate = productInfoMapper.selectAvgRate();

        // 累计成交金额
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();

        BaseInfo baseInfo = new BaseInfo(avgRate, sumBidMoney, registerUser);

        return baseInfo;
    }
}

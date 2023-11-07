package com.powernode.dataservice.mapper;

import com.powernode.api.model.BidInfo;
import com.powernode.api.pojo.BidInfoProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {
    // 累计成交金额
    BigDecimal selectSumBidMoney();

    // 查询某个产品的投资记录
    List<BidInfoProduct> selectByProductId(@Param("productId") Integer productId, @Param("offset") int offset, @Param("rows") Integer rows);

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


    // 某个产品的投资记录
    List<BidInfo> selectByProdId(@Param("productId") Integer productId);
}
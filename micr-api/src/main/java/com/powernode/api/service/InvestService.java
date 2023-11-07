package com.powernode.api.service;

import com.powernode.api.pojo.BidInfoProduct;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {
    // 查询某个产品的投资记录
    List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize);

    // 投资理财产品, int 是投资的结果， 1 投资成功
    int investProduct(Integer uid, Integer productId, BigDecimal money);
}

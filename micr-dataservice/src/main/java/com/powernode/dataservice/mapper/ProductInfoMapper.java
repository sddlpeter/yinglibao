package com.powernode.dataservice.mapper;

import com.powernode.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {
    // 利率平均值
    BigDecimal selectAvgRate();

    // 按产品类型分页查询
    List<ProductInfo> selectByTypeLimit(@Param("ptype") Integer ptype,
                                        @Param("offset") Integer offset,
                                        @Param("rows") Integer rows);

    // 某个产品类型的记录总数
    Integer selectCountByType(@Param("ptype") Integer pType);


    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);


    // 扣除产品剩余可投资金额
    int updateLeftProductMoney(@Param("id") Integer productId, @Param("money") BigDecimal money);

    // 更新为满标的状态
    int updateSold(Integer productId);

    // 满标的理财列表
    List<ProductInfo> selectFullTimeProducts(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);
}
package com.powernode.front.controller;

import com.powernode.api.model.ProductInfo;
import com.powernode.api.pojo.BidInfoProduct;
import com.powernode.api.pojo.MultiProduct;
import com.powernode.common.enums.RCode;
import com.powernode.common.util.CommonUtil;
import com.powernode.front.view.PageInfo;
import com.powernode.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin
@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController{

    @ApiOperation(value = "首页三类产品列表", notes = "一个新手宝， 三个优选， 三个散标产品")
    @GetMapping("/product/index")
    public RespResult queryProductIndex() {
        RespResult result = RespResult.ok();
        MultiProduct multiProduct = productService.queryIndexPageProducts();
        result.setData(multiProduct);
        return result;
    }

    // 按产品类型分页查询接口
    @ApiOperation(value = "产品分页查询", notes = "按产品类型分页查询")
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam("ptype") Integer pType,
                                         @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (pType != null && (pType == 0 || pType == 1 || pType == 2)) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);

            // 分页处理，记录总数
            Integer recordNums = productService.queryRecordNumsByType(pType);
            if (recordNums > 0) {
                // 产品集合
                List<ProductInfo> productInfos = productService.queryByTypeLimit(pType, pageNo, pageSize);
                PageInfo page = new PageInfo(pageNo, pageSize, recordNums);

                // 构建pageInfo
                result = RespResult.ok();
                result.setList(productInfos);
                result.setPage(page);
            }

        } else {
            // 请求参数有误
            result.setRCode(RCode.REQUEST_PRODUCT_TYPE_ERR);
        }
        return result;
    }

    // 查询某个产品的详情和投资记录
    @ApiOperation(value = "产品的详情", notes = "某个产品的详细信息和投资记录5条")
    @GetMapping("/product/info")
    public RespResult queryProductDetail(@RequestParam("productId") Integer id) {
        RespResult result = RespResult.fail();
        if (id != null && id > 0) {
            // 调用产品的查询
            ProductInfo productInfo = productService.queryById(id);

            // 不为空，查询成功
            if (productInfo != null) {
                // 查询产品的投资记录
                List<BidInfoProduct> bidInfoList = investService.queryBidListByProductId(id, 1, 5);
                result = RespResult.ok();
                result.setData(productInfo);
                result.setList(bidInfoList);
            } else {
                result.setRCode(RCode.PRODUCT_OFFLINE);
            }
        }
        return result;
    }

}

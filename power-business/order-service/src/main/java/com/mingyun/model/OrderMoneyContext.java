package com.mingyun.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMoneyContext {

    // 条件属性
    private List<SkuBuyModel> skuBuyModels;

    private String openId;


    // 金额项属性

    // 商品金额
    private BigDecimal totalMoney;

    // 折扣金额
    private BigDecimal shopReduce;

    // 优惠券的选择
    private List<Long> couponIds;
    // 优惠券金额
    private BigDecimal couponMoney;

    // 运费金额
    private BigDecimal transfee;

    // 最终的实际金额
    private BigDecimal actualTotal;

}

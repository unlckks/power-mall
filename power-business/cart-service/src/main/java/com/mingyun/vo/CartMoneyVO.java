package com.mingyun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("购物车价格")
public class CartMoneyVO {

    @ApiModelProperty("商品总价")
    private BigDecimal totalMoney = BigDecimal.ZERO;

    @ApiModelProperty("最终金额")
    private BigDecimal finalMoney = BigDecimal.ZERO;

    @ApiModelProperty("运费金额")
    private BigDecimal transMoney = BigDecimal.ZERO;

}

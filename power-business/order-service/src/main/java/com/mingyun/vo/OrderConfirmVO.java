package com.mingyun.vo;

import com.mingyun.domain.MemberAddr;
import com.mingyun.model.ShopOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单确认返回值")
public class OrderConfirmVO {

    //  默认收获地址
    @ApiModelProperty("默认收获地址")
    private MemberAddr memberAddr;

    // 店铺的集合 （商品的集合）
    @ApiModelProperty("店铺的集合")
    private List<ShopOrder> shopOrders;

    // 商品总数
    @ApiModelProperty("商品总数")
    private Integer totalCount;

    @ApiModelProperty("商品金额")
    private BigDecimal totalMoney;

    @ApiModelProperty("实付金额")
    private BigDecimal actualTotal;

    @ApiModelProperty("运费金额")
    private BigDecimal transfee;

    @ApiModelProperty("折扣金额")
    private BigDecimal shopReduce;
}

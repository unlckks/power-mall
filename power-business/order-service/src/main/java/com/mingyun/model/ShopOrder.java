package com.mingyun.model;

import com.mingyun.domain.OrderItem;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiOperation("店铺订单模型")
public class ShopOrder {


    @ApiModelProperty("店铺的id")
    private Long shopId;
    @ApiModelProperty("店铺的名称")
    private String shopName;
    @ApiModelProperty("店铺的logo")
    private String shopLogo;
    @ApiModelProperty("店铺的连接")
    private String shopLink;

    @ApiModelProperty("店铺的商品集合")
    private List<OrderItem> shopOrderItems;
}

package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("店铺购物车")
public class ShopCart {
    @ApiModelProperty("店铺的id")
    private Long shopId;
    @ApiModelProperty("店铺的名称")
    private String shopName;
    @ApiModelProperty("店铺的logo")
    private String shopLogo;
    @ApiModelProperty("店铺的连接")
    private String shopLink;
    @ApiModelProperty("店铺的商品集合")
    private List<CartItem> shopCartItems;
}

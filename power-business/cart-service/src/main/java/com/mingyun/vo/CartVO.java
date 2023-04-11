package com.mingyun.vo;

import com.mingyun.model.ShopCart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("店铺购详情")
public class CartVO {

    @ApiModelProperty("店铺的集合")
    private List<ShopCart> shopCarts;

    // 商城优惠券 商城的活动满减...
}


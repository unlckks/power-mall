package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 20:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("配送方式")
public class DeliveryMode {

    @ApiModelProperty("商家配送")
    private Boolean hasShopDelivery;

    @ApiModelProperty("用户自提")
    private Boolean hasUserPickUp;

}
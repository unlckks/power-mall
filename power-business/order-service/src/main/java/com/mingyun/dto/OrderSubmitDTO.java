package com.mingyun.dto;

import com.mingyun.domain.MemberAddr;
import com.mingyun.model.ShopOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("下预订单的对象")
public class OrderSubmitDTO {
    @ApiModelProperty("默认收获地址")
    private MemberAddr memberAddr;

    @ApiModelProperty("店铺的集合")
    private List<ShopOrder> shopOrders;

    @ApiModelProperty("买家备注")
    private String remarks;

    @ApiModelProperty("下单入口 0 购物车 1 单品")
    private Integer orderEntry;
    // 优惠券ids
}

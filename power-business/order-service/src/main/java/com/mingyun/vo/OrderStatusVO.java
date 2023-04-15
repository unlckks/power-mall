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
 * @Date: 2023-04-13 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单各个状态数量对象")
public class OrderStatusVO {

    @ApiModelProperty("待支付")
    private Long unPay = 0L;
    @ApiModelProperty("待发货")
    private Long payed = 0L;
    @ApiModelProperty("待收获")
    private Long consignment = 0L;


}
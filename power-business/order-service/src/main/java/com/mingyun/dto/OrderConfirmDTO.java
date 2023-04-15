package com.mingyun.dto;

import com.mingyun.model.ProdConfirmModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单确认对象")
public class OrderConfirmDTO {

    @ApiModelProperty("购物车ids")
    private List<Long> basketIds;

    @ApiModelProperty("单品确认项")
    private ProdConfirmModel orderItem;


}

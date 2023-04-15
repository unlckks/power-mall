package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("单品确认对象")
public class ProdConfirmModel {

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("数量")
    private Integer prodCount;

}

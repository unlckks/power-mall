package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuBuyModel {

    private Long skuId;
    private BigDecimal price;
    private Integer count;

}

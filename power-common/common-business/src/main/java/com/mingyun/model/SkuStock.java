package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("sku库存对象")
public class SkuStock {

    private Long skuId;
    private Integer count;
    private String prodName;


}

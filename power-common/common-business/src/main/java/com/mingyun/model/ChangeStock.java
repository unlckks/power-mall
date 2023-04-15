package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("库存修改对象")
public class ChangeStock {
    private List<SkuStock> skuStocks;
    private List<ProdStock> prodStocks;
}

package com.mingyun.vo;

import com.mingyun.domain.Sku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品详情返回对象")
public class ProdSkuVO {

    @ApiModelProperty("sku集合")
    private List<Sku> skuList;
}

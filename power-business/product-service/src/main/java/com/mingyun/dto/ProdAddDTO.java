package com.mingyun.dto;

import com.mingyun.domain.Prod;
import com.mingyun.domain.Sku;
import com.mingyun.model.DeliveryMode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 19:56
 */
/**
 * @Author: MingYun
 * @Date: 2023-04-06 19:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品新增对象")
public class ProdAddDTO extends Prod {

    @ApiModelProperty("标签分组ids")
    private List<Long> tagList;

    @ApiModelProperty("sku集合")
    private List<Sku> skuList;

    @ApiModelProperty("配送方式")
    private DeliveryMode deliveryModeVo;


}

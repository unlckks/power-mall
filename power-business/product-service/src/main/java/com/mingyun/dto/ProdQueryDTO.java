package com.mingyun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-06 11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品查询对象")
public class ProdQueryDTO {

    @ApiModelProperty("商品名称")
    private String prodName;
    @ApiModelProperty("商品状态")
    private Integer status;

}

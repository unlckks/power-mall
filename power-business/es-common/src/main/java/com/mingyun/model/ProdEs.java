package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 19:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品检索对象")
public class ProdEs {


    @ApiModelProperty(value = "产品ID")
    private Long prodId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;


    /**
     * 现价
     */
    @ApiModelProperty(value = "现价")
    private BigDecimal price;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品分类")
    private Long categoryId;

    @ApiModelProperty(value = "销量")
    private Integer soldNum;

    @ApiModelProperty(value = "标签ids")
    private List<Long> tagList;

    @ApiModelProperty(value = "好评率")
    private BigDecimal positiveRating;

    @ApiModelProperty(value = "好评数")
    private Long praiseNumber;

}

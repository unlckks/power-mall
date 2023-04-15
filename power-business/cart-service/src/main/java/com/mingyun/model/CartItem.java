package com.mingyun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("购物车商品条目")
public class CartItem {

    /**
     * 主键
     */
    @TableId(value = "basket_id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long basketId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺ID")
    private Long shopId;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    @ApiModelProperty(value="产品ID")
    private Long prodId;

    /**
     * SkuID
     */
    @TableField(value = "sku_id")
    @ApiModelProperty(value="SkuID")
    private Long skuId;

    /**
     * 购物车产品个数
     */
    @TableField(value = "prod_count")
    @ApiModelProperty(value="购物车产品个数")
    private Integer prodCount;


    /**
     * 购物时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="购物时间")
    private Date createTime;

    //////////////////////

    /**
     * 价格
     */
    @TableField(value = "price")
    @ApiModelProperty(value="价格")
    private BigDecimal price;

    /**
     * sku图片
     */
    @TableField(value = "pic")
    @ApiModelProperty(value="sku图片")
    private String pic;


    /**
     * sku名称
     */
    @TableField(value = "sku_name")
    @ApiModelProperty(value="sku名称")
    private String skuName;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    @ApiModelProperty(value="商品名称")
    private String prodName;


}

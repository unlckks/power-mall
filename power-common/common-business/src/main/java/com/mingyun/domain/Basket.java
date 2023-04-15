package com.mingyun.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */

/**
    * 购物车
    */
@ApiModel(description="购物车")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "basket")
public class Basket implements Serializable {
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
     * 用户ID
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="用户ID")
    private String openId;

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

    private static final long serialVersionUID = 1L;

    public static final String COL_BASKET_ID = "basket_id";

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_PROD_ID = "prod_id";

    public static final String COL_SKU_ID = "sku_id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_PROD_COUNT = "prod_count";

    public static final String COL_CREATE_TIME = "create_time";
}
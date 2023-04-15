package com.mingyun.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-13 15:00
 */
@ApiModel(description="order_settlement")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_settlement")
public class OrderSettlement implements Serializable {
    /**
     * 支付结算单据ID
     */
    @TableId(value = "settlement_id", type = IdType.AUTO)
    @ApiModelProperty(value="支付结算单据ID")
    private Long settlementId;

    /**
     * 外部订单流水号
     */
    @TableField(value = "biz_pay_no")
    @ApiModelProperty(value="外部订单流水号")
    private String bizPayNo;

    /**
     * order表中的订单号
     */
    @TableField(value = "order_number")
    @ApiModelProperty(value="order表中的订单号")
    private String orderNumber;

    /**
     * 支付方式 1 支付宝 2 微信
     */
    @TableField(value = "pay_type")
    @ApiModelProperty(value="支付方式 1 支付宝 2 微信")
    private Integer payType;

    /**
     * 支付金额
     */
    @TableField(value = "pay_amount")
    @ApiModelProperty(value="支付金额")
    private BigDecimal payAmount;

    /**
     * 是否清算 0:否 1:是
     */
    @TableField(value = "is_clearing")
    @ApiModelProperty(value="是否清算 0:否 1:是")
    private Integer isClearing;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 清算时间
     */
    @TableField(value = "clearing_time")
    @ApiModelProperty(value="清算时间")
    private Date clearingTime;

    /**
     * 版本号
     */
    @TableField(value = "version")
    @ApiModelProperty(value="版本号")
    private Integer version;

    /**
     * 支付状态
     */
    @TableField(value = "pay_status")
    @ApiModelProperty(value="支付状态")
    private Integer payStatus;

    private static final long serialVersionUID = 1L;

    public static final String COL_SETTLEMENT_ID = "settlement_id";

    public static final String COL_BIZ_PAY_NO = "biz_pay_no";

    public static final String COL_ORDER_NUMBER = "order_number";

    public static final String COL_PAY_TYPE = "pay_type";

    public static final String COL_PAY_AMOUNT = "pay_amount";

    public static final String COL_IS_CLEARING = "is_clearing";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CLEARING_TIME = "clearing_time";

    public static final String COL_VERSION = "version";

    public static final String COL_PAY_STATUS = "pay_status";
}
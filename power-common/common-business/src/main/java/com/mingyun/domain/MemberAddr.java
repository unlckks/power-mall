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
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:03
 */
/**
 * 用户配送地址
 */
@ApiModel(description = "用户配送地址")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "member_addr")
@Validated
public class MemberAddr implements Serializable {
    /**
     * ID
     */
    @TableId(value = "addr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long addrId;

    /**
     * 用户ID
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "用户ID")
    private String openId;

    /**
     * 收货人
     */
    @TableField(value = "receiver")
    @ApiModelProperty(value = "收货人")
    @NotBlank(message = "收货人不能为空")
    private String receiver;

    /**
     * 省ID
     */
    @TableField(value = "province_id")
    @ApiModelProperty(value = "省ID")
    private Long provinceId;

    /**
     * 省
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 城市ID
     */
    @TableField(value = "city_id")
    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    /**
     * 区
     */
    @TableField(value = "area")
    @ApiModelProperty(value = "区")
    private String area;

    /**
     * 区ID
     */
    @TableField(value = "area_id")
    @ApiModelProperty(value = "区ID")
    private Long areaId;

    /**
     * 邮编
     */
    @TableField(value = "post_code")
    @ApiModelProperty(value = "邮编")
    private String postCode;

    /**
     * 地址
     */
    @TableField(value = "addr")
    @ApiModelProperty(value = "地址")
    private String addr;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value = "手机")
    @Length(min = 11, max = 11, message = "手机格式不正确")
    private String mobile;

    /**
     * 状态,1正常，0无效
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态,1正常，0无效")
    private Integer status;

    /**
     * 是否默认地址 1是
     */
    @TableField(value = "common_addr")
    @ApiModelProperty(value = "是否默认地址 1是")
    private Integer commonAddr;

    /**
     * 建立时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "建立时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ADDR_ID = "addr_id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_RECEIVER = "receiver";

    public static final String COL_PROVINCE_ID = "province_id";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_CITY_ID = "city_id";

    public static final String COL_AREA = "area";

    public static final String COL_AREA_ID = "area_id";

    public static final String COL_POST_CODE = "post_code";

    public static final String COL_ADDR = "addr";

    public static final String COL_MOBILE = "mobile";

    public static final String COL_STATUS = "status";

    public static final String COL_COMMON_ADDR = "common_addr";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:25
 */
@ApiModel(description="shop_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shop_detail")
public class ShopDetail implements Serializable {
    /**
     * 店铺id
     */
    @TableId(value = "shop_id", type = IdType.AUTO)
    @ApiModelProperty(value="店铺id")
    private Long shopId;

    /**
     * 店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    @TableField(value = "shop_name")
    @ApiModelProperty(value="店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一")
    private String shopName;

    /**
     * 店长用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="店长用户id")
    private String userId;

    /**
     * 店铺类型
     */
    @TableField(value = "shop_type")
    @ApiModelProperty(value="店铺类型")
    private Integer shopType;

    /**
     * 店铺简介(可修改)
     */
    @TableField(value = "intro")
    @ApiModelProperty(value="店铺简介(可修改)")
    private String intro;

    /**
     * 店铺公告(可修改)
     */
    @TableField(value = "shop_notice")
    @ApiModelProperty(value="店铺公告(可修改)")
    private String shopNotice;

    /**
     * 店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    @TableField(value = "shop_industry")
    @ApiModelProperty(value="店铺行业(餐饮、生鲜果蔬、鲜花等)")
    private Integer shopIndustry;

    /**
     * 店长
     */
    @TableField(value = "shop_owner")
    @ApiModelProperty(value="店长")
    private String shopOwner;

    /**
     * 店铺绑定的手机(登录账号：唯一)
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value="店铺绑定的手机(登录账号：唯一)")
    private String mobile;

    /**
     * 店铺联系电话
     */
    @TableField(value = "tel")
    @ApiModelProperty(value="店铺联系电话")
    private String tel;

    /**
     * 店铺所在纬度(可修改)
     */
    @TableField(value = "shop_lat")
    @ApiModelProperty(value="店铺所在纬度(可修改)")
    private String shopLat;

    /**
     * 店铺所在经度(可修改)
     */
    @TableField(value = "shop_lng")
    @ApiModelProperty(value="店铺所在经度(可修改)")
    private String shopLng;

    /**
     * 店铺详细地址
     */
    @TableField(value = "shop_address")
    @ApiModelProperty(value="店铺详细地址")
    private String shopAddress;

    /**
     * 店铺所在省份（描述）
     */
    @TableField(value = "province")
    @ApiModelProperty(value="店铺所在省份（描述）")
    private String province;

    /**
     * 店铺所在城市（描述）
     */
    @TableField(value = "city")
    @ApiModelProperty(value="店铺所在城市（描述）")
    private String city;

    /**
     * 店铺所在区域（描述）
     */
    @TableField(value = "area")
    @ApiModelProperty(value="店铺所在区域（描述）")
    private String area;

    /**
     * 店铺省市区代码，用于回显
     */
    @TableField(value = "pca_code")
    @ApiModelProperty(value="店铺省市区代码，用于回显")
    private String pcaCode;

    /**
     * 店铺logo(可修改)
     */
    @TableField(value = "shop_logo")
    @ApiModelProperty(value="店铺logo(可修改)")
    private String shopLogo;

    /**
     * 店铺相册
     */
    @TableField(value = "shop_photos")
    @ApiModelProperty(value="店铺相册")
    private String shopPhotos;

    /**
     * 每天营业时间段(可修改)
     */
    @TableField(value = "open_time")
    @ApiModelProperty(value="每天营业时间段(可修改)")
    private String openTime;

    /**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    @TableField(value = "shop_status")
    @ApiModelProperty(value="店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改")
    private Integer shopStatus;

    /**
     * 0:商家承担运费; 1:买家承担运费
     */
    @TableField(value = "transport_type")
    @ApiModelProperty(value="0:商家承担运费; 1:买家承担运费")
    private Integer transportType;

    /**
     * 固定运费
     */
    @TableField(value = "fixed_freight")
    @ApiModelProperty(value="固定运费")
    private BigDecimal fixedFreight;

    /**
     * 满X包邮
     */
    @TableField(value = "full_free_shipping")
    @ApiModelProperty(value="满X包邮")
    private BigDecimal fullFreeShipping;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 分销开关(0:开启 1:关闭)
     */
    @TableField(value = "is_distribution")
    @ApiModelProperty(value="分销开关(0:开启 1:关闭)")
    private Integer isDistribution;

    private static final long serialVersionUID = 1L;

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_SHOP_NAME = "shop_name";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_SHOP_TYPE = "shop_type";

    public static final String COL_INTRO = "intro";

    public static final String COL_SHOP_NOTICE = "shop_notice";

    public static final String COL_SHOP_INDUSTRY = "shop_industry";

    public static final String COL_SHOP_OWNER = "shop_owner";

    public static final String COL_MOBILE = "mobile";

    public static final String COL_TEL = "tel";

    public static final String COL_SHOP_LAT = "shop_lat";

    public static final String COL_SHOP_LNG = "shop_lng";

    public static final String COL_SHOP_ADDRESS = "shop_address";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_AREA = "area";

    public static final String COL_PCA_CODE = "pca_code";

    public static final String COL_SHOP_LOGO = "shop_logo";

    public static final String COL_SHOP_PHOTOS = "shop_photos";

    public static final String COL_OPEN_TIME = "open_time";

    public static final String COL_SHOP_STATUS = "shop_status";

    public static final String COL_TRANSPORT_TYPE = "transport_type";

    public static final String COL_FIXED_FREIGHT = "fixed_freight";

    public static final String COL_FULL_FREE_SHIPPING = "full_free_shipping";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DISTRIBUTION = "is_distribution";
}
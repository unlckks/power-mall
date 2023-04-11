package com.mingyun.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
/**
    * 用户表
    */
@ApiModel(description="用户表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`member`")
public class Member implements Serializable {
    /**
     * 会员表的主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="会员表的主键")
    private Long id;

    /**
     * ID
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="ID")
    private String openId;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value="用户昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value="真实姓名")
    private String realName;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_mail")
    @ApiModelProperty(value="用户邮箱")
    private String userMail;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    @ApiModelProperty(value="支付密码")
    private String payPassword;

    /**
     * 手机号码
     */
    @TableField(value = "user_mobile")
    @ApiModelProperty(value="手机号码")
    private String userMobile;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="修改时间")
    private Date updateTime;

    /**
     * 注册时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="注册时间")
    private Date createTime;

    /**
     * 注册IP
     */
    @TableField(value = "user_regip")
    @ApiModelProperty(value="注册IP")
    private String userRegip;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    @ApiModelProperty(value="最后登录时间")
    private Date userLasttime;

    /**
     * 最后登录IP
     */
    @TableField(value = "user_lastip")
    @ApiModelProperty(value="最后登录IP")
    private String userLastip;

    /**
     * M(男) or F(女)
     */
    @TableField(value = "sex")
    @ApiModelProperty(value="M(男) or F(女)")
    private String sex;

    /**
     * 例如：2009-11-27
     */
    @TableField(value = "birth_date")
    @ApiModelProperty(value="例如：2009-11-27")
    private String birthDate;

    /**
     * 头像图片路径
     */
    @TableField(value = "pic")
    @ApiModelProperty(value="头像图片路径")
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态 1 正常 0 无效")
    private Integer status;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    @ApiModelProperty(value="用户积分")
    private Integer score;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_NICK_NAME = "nick_name";

    public static final String COL_REAL_NAME = "real_name";

    public static final String COL_USER_MAIL = "user_mail";

    public static final String COL_PAY_PASSWORD = "pay_password";

    public static final String COL_USER_MOBILE = "user_mobile";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_USER_REGIP = "user_regip";

    public static final String COL_USER_LASTTIME = "user_lasttime";

    public static final String COL_USER_LASTIP = "user_lastip";

    public static final String COL_SEX = "sex";

    public static final String COL_BIRTH_DATE = "birth_date";

    public static final String COL_PIC = "pic";

    public static final String COL_STATUS = "status";

    public static final String COL_SCORE = "score";
}
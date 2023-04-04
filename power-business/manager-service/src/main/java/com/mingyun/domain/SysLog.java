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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
/**
    * 系统日志
    */
@ApiModel(description="系统日志")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_log")
public class SysLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户名")
    private Long userId;

    /**
     * 用户操作
     */
    @TableField(value = "`operation`")
    @ApiModelProperty(value="用户操作")
    private String operation;

    /**
     * 请求方法
     */
    @TableField(value = "`method`")
    @ApiModelProperty(value="请求方法")
    private String method;

    /**
     * 请求参数
     */
    @TableField(value = "params")
    @ApiModelProperty(value="请求参数")
    private String params;

    /**
     * 执行时长(毫秒)
     */
    @TableField(value = "`time`")
    @ApiModelProperty(value="执行时长(毫秒)")
    private Long time;

    /**
     * IP地址
     */
    @TableField(value = "ip")
    @ApiModelProperty(value="IP地址")
    private String ip;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    @ApiModelProperty(value="创建时间")
    private Date createDate;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_OPERATION = "operation";

    public static final String COL_METHOD = "method";

    public static final String COL_PARAMS = "params";

    public static final String COL_TIME = "time";

    public static final String COL_IP = "ip";

    public static final String COL_CREATE_DATE = "create_date";
}
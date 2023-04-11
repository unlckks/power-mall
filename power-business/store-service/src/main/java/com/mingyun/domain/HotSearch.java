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
 *  @Date: 2023-04-07 19:40
 */
/**
    * 热搜
    */
@ApiModel(description="热搜")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "hot_search")
public class HotSearch implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "hot_search_id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long hotSearchId;

    /**
     * 店铺ID 0为全局热搜
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺ID 0为全局热搜")
    private Long shopId;

    /**
     * 内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value="内容")
    private String content;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="录入时间")
    private Date createTime;

    /**
     * 顺序
     */
    @TableField(value = "seq")
    @ApiModelProperty(value="顺序")
    private Integer seq;

    /**
     * 状态 0下线 1上线
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态 0下线 1上线")
    private Byte status;

    /**
     * 热搜标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="热搜标题")
    private String title;

    private static final long serialVersionUID = 1L;

    public static final String COL_HOT_SEARCH_ID = "hot_search_id";

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_CONTENT = "content";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_SEQ = "seq";

    public static final String COL_STATUS = "status";

    public static final String COL_TITLE = "title";
}
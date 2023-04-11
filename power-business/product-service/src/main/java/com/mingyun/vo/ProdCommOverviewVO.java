package com.mingyun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("评论总览返回对象")
public class ProdCommOverviewVO {
    @ApiModelProperty("好评率")
    private BigDecimal goodLv = BigDecimal.ZERO;
    @ApiModelProperty("总评数")
    private Long allCount = 0L;
    @ApiModelProperty("好评数")
    private Long goodCount = 0L;
    @ApiModelProperty("中评数")
    private Long secondCount = 0L;
    @ApiModelProperty("差评数")
    private Long badCount = 0L;
    @ApiModelProperty("带图数")
    private Long picCount = 0L;
}

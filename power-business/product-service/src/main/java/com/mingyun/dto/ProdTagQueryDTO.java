package com.mingyun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("活动标签查询对象")
public class ProdTagQueryDTO {

    @ApiModelProperty("标签名称")
    private String title;
    @ApiModelProperty("标签状态")
    private Integer status;

}

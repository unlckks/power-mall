package com.mingyun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 19:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页对象")
public class PageDTO {

    @ApiModelProperty("当前页")
    private Integer current = 1;

    @ApiModelProperty("每页条数")
    private Integer size = 10;

}
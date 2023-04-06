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
@ApiModel("规格查询对象")
public class PropQueryDTO {

    @ApiModelProperty("规则名称")
    private String propName;
}

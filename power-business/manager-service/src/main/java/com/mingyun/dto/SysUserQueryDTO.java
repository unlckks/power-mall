package com.mingyun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("管理员查询入参对象")
public class SysUserQueryDTO {

    @ApiModelProperty("用户名")
    private String username;
}

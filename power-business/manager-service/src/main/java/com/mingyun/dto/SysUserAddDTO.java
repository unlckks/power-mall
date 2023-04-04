package com.mingyun.dto;

import com.mingyun.domain.SysUser;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiOperation("管理员新增入参对象")
public class SysUserAddDTO extends SysUser {
    @ApiModelProperty("角色ids")
    private List<Long> roleIdList;
}

package com.mingyun.vo;

import com.mingyun.domain.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiOperation("菜单和权限返回对象")
public class MenuPermsVO {
    //菜单 树结构
    @ApiModelProperty("菜单树结构")
    private List<SysMenu> menuList;
    //权限
    @ApiModelProperty("权限集合")
    private Set<String> authorities;
}

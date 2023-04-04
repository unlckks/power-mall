package com.mingyun.controller;

import com.mingyun.domain.SysRole;
import com.mingyun.model.Result;
import com.mingyun.service.SysMenuService;
import com.mingyun.service.SysRoleService;
import com.mingyun.vo.MenuPermsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 16:14
 */
@RestController
@Api(tags = "角色管理接口")
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询角色
     * @return
     */
    @GetMapping("list")
    @ApiOperation("查询角色列表")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<List<SysRole>> loadSysRoleList() {
        List<SysRole> sysRoles = sysRoleService.loadSysRoleList();
        return Result.success(sysRoles);
    }

}

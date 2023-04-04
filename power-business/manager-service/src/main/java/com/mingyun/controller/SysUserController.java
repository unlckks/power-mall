package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.SysUser;
import com.mingyun.dto.SysUserAddDTO;
import com.mingyun.dto.SysUserQueryDTO;
import com.mingyun.model.Result;
import com.mingyun.service.SysUserService;
import com.mingyun.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 16:27
 */
@RestController
@Api(tags = "用户管理接口")
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("info")
    @ApiOperation("查询当前用户的信息")
    public Result<SysUser> getUserInfo() {
        Long userId = AuthUtil.getUserId();
        SysUser user = sysUserService.getById(userId);
        return Result.success(user);
    }

    /**
     * 分页查询
     *
     * @param pageDTO
     * @param sysUserQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询管理员列表")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<Page<SysUser>> loadSysUserPage(PageDTO pageDTO, SysUserQueryDTO sysUserQueryDTO) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SysUser> sysUserPage = sysUserService.loadSysUserPage(pageDTO, sysUserQueryDTO);
        return Result.success(sysUserPage);
    }

    /**
     * 新增管理员
     *
     * @param sysUserAddDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增管理员")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<String> addSysUser(@RequestBody @Validated SysUserAddDTO sysUserAddDTO) {
        Integer i = sysUserService.addSysUser(sysUserAddDTO);
        return Result.handle(i > 0);
    }
}

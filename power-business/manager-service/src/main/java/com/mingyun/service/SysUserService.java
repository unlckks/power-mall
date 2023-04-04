package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.SysUserAddDTO;
import com.mingyun.dto.SysUserQueryDTO;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
public interface SysUserService extends IService<SysUser>{

    /**
     * 分页查询
     * @param pageDTO
     * @param sysUserQueryDTO
     * @return
     */
        Page<SysUser> loadSysUserPage(PageDTO pageDTO, SysUserQueryDTO sysUserQueryDTO);

    /**
     * 新增管理员
     * @param sysUserAddDTO
     * @return
     */
    Integer addSysUser(SysUserAddDTO sysUserAddDTO);


}

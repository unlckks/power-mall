package com.mingyun.service;

import com.mingyun.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
public interface SysRoleService extends IService<SysRole>{

    /**
     * 查询角色
     * @return
     */
        List<SysRole> loadSysRoleList();
}

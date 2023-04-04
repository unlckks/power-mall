package com.mingyun.service;

import com.mingyun.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
public interface SysMenuService extends IService<SysMenu>{
    /**
     * 查菜单数据
     * @param userId
     * @return
     */
        List<SysMenu> findUserMenus(Long userId);
    }

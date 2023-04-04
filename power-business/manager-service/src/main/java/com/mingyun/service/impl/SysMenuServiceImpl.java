package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.SysMenuMapper;
import com.mingyun.domain.SysMenu;
import com.mingyun.service.SysMenuService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 10:53
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.SysMenuServiceImpl")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 查菜单数据
     * 转树结构
     * 考虑缓存
     * @param userId
     * @return
     */
    @Override
    @Cacheable(key = "#userId")
    public List<SysMenu> findUserMenus(Long userId) {
        List<SysMenu> sysMenus = sysMenuMapper.selectMenusByUserId(userId);
        if (!CollectionUtils.isEmpty(sysMenus)) {
            // 转成树形结构
            sysMenus = translateMenuTree(sysMenus, 0L);
        }
        return sysMenus;
    }

    /**
     * 转树形结构
     * @param sysMenus
     * @param
     * @return
     */
    private List<SysMenu> translateMenuTree(List<SysMenu> sysMenus, long pid) {
        //进行递归
        List<SysMenu> roots = sysMenus.stream()
                .filter(sysMenu -> sysMenu.getParentId().equals(pid))
                .collect(Collectors.toList());
        roots.forEach(r -> r.setList(translateMenuTree(sysMenus, r.getMenuId())));
        return roots;
    }
}

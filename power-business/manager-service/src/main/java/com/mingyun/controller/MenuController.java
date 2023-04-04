package com.mingyun.controller;

import com.mingyun.domain.SysMenu;
import com.mingyun.mapper.SysMenuMapper;
import com.mingyun.model.Result;
import com.mingyun.service.SysMenuService;
import com.mingyun.util.AuthUtil;
import com.mingyun.vo.MenuPermsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 10:55
 */
@RestController
@Api(tags = "菜单管理接口")
@RequestMapping("sys/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查菜单数据
     * 树形结构 后端来做
     * 拿权限集合
     * @return
     */
    @GetMapping("nav")
    @ApiOperation("加载当前用户的菜单和权限集合")
    public Result<MenuPermsVO> loadMenuAndPerms(){
        Long userId = AuthUtil.getUserId();
        //进行查菜单
        List<SysMenu> sysMenus =   sysMenuService.findUserMenus(userId);
        Set<String> perms = AuthUtil.getPerms();
        MenuPermsVO menuPermsVO = new MenuPermsVO(sysMenus, perms);
        return Result.success(menuPermsVO);
    }
}

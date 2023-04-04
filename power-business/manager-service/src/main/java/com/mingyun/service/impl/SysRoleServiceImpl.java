package com.mingyun.service.impl;

import com.mingyun.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.SysRoleMapper;
import com.mingyun.domain.SysRole;
import com.mingyun.service.SysRoleService;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 10:53
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 查询角色
     * 全查询
     * 加缓存
     *
     * @return
     */
    @Override
    public List<SysRole> loadSysRoleList() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        Long shopId = AuthUtil.getShopId();
        if (!shopId.equals(1L)) {
            sysRoles = sysRoles.stream()
                    .filter(sysRole -> !sysRole.getRoleId().equals(1L))
                    .collect(Collectors.toList());
        }
        return sysRoles;
    }

}

package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.SysRole;
import com.mingyun.domain.SysUserRole;
import com.mingyun.dto.SysUserAddDTO;
import com.mingyun.dto.SysUserQueryDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.mapper.SysUserRoleMapper;
import com.mingyun.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.SysUser;
import com.mingyun.mapper.SysUserMapper;
import com.mingyun.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 10:53
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 分页查询
     *
     * @param pageDTO
     * @param sysUserQueryDTO
     * @return
     */
    @Override
    public Page<SysUser> loadSysUserPage(PageDTO pageDTO, SysUserQueryDTO sysUserQueryDTO) {
        Page<SysUser> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Long shopId = AuthUtil.getShopId();
        return sysUserMapper.selectPage(page, new LambdaQueryWrapper<SysUser>()
                .eq(!shopId.equals(1L), SysUser::getShopId, shopId)
                .like(StringUtils.hasText(sysUserQueryDTO.getUsername()), SysUser::getUsername, sysUserQueryDTO.getUsername())
                .orderByDesc(SysUser::getCreateTime)
        );
    }

    /**
     * 新增管理员
     * @param sysUserAddDTO
     * @return
     */
    @Override
    @Transactional
    public Integer addSysUser(SysUserAddDTO sysUserAddDTO) {
            //校验
        Long count = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUserAddDTO.getUsername())
        );
        if(count >0){
            throw  new BusinessException("当前用户已经存在");
        }
        Long userId = AuthUtil.getUserId();
        sysUserAddDTO.setCreateTime(new Date());
        sysUserAddDTO.setCreateUserId(userId);
        //进行加密
        String encode = new BCryptPasswordEncoder().encode(sysUserAddDTO.getPassword());
        sysUserAddDTO.setPassword(encode);
        int insert = sysUserMapper.insert(sysUserAddDTO);
        //进行处理角色的关系
        List<Long> roleIdList = sysUserAddDTO.getRoleIdList();
        if (!CollectionUtils.isEmpty(roleIdList)){
            roleIdList.forEach(rid ->{
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(rid);
                sysUserRole.setRoleId(sysUserAddDTO.getUserId());
                sysUserRoleMapper.insert(sysUserRole);
            });
        }
        return insert;
    }


}


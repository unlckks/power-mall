package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.LoginSysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-01 20:44
 */
@Mapper
public interface LoginSysUserMapper extends BaseMapper<LoginSysUser> {
    Set<String> selectPermsBySysUserId(Long userId);
}
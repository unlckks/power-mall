package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.SysUser;
import com.mingyun.mapper.SysUserMapper;
import com.mingyun.service.SysUserService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

}

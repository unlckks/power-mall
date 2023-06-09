package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-04 10:53
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查菜单数据
     * @param userId
     * @return
     */
    List<SysMenu> selectMenusByUserId(Long userId);
}
package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
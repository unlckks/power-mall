package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Mapper
public interface ProdMapper extends BaseMapper<Prod> {

    List<Prod> selectMyPage(@Param("offset") int offset, @Param("size") int size, @Param("t") Date t1);
}
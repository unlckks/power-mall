package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
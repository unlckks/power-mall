package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-13 15:00
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
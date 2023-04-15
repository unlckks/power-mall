package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.OrderItem;
import com.mingyun.mapper.OrderItemMapper;
import com.mingyun.service.OrderItemService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-13 15:00
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService{

}

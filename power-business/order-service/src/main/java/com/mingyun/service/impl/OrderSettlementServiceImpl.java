package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.OrderSettlement;
import com.mingyun.mapper.OrderSettlementMapper;
import com.mingyun.service.OrderSettlementService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-13 15:00
 */
@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService{

}

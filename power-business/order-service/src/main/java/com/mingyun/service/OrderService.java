package com.mingyun.service;

import com.mingyun.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.OrderConfirmDTO;
import com.mingyun.dto.OrderSubmitDTO;
import com.mingyun.model.ChangeStock;
import com.mingyun.vo.OrderConfirmVO;
import com.mingyun.vo.OrderStatusVO;

import java.util.Map;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-13 15:00
 */
public interface OrderService extends IService<Order>{

    /**
     * 查询订单数量
     * @return
     */
    OrderStatusVO orderStatusCount();

    /**
     * 订单确认
     * @param orderConfirmDTO
     * @return
     */
    OrderConfirmVO orderConfirm(OrderConfirmDTO orderConfirmDTO);

    /**
     * 下预订单
     *
     * @param orderSubmitDTO
     * @return
     */
    Map<String, String> orderSubmit(OrderSubmitDTO orderSubmitDTO);

    /**
     * 回滚订单
     *
     * @param order
     * @param changeStock
     */
    void realDoBack(Order order, ChangeStock changeStock);

}

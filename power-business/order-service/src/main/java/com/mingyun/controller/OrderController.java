package com.mingyun.controller;

import com.mingyun.dto.OrderConfirmDTO;
import com.mingyun.model.Result;
import com.mingyun.service.OrderService;
import com.mingyun.vo.OrderConfirmVO;
import com.mingyun.vo.OrderStatusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:00
 */
@RestController
@Api(tags = "订单管理接口")
@RequestMapping("p/myOrder")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 进行查询订单状态
     *
     * @return
     */
    @GetMapping("orderCount")
    @ApiOperation("查询订单状态数量")
    public Result<OrderStatusVO> orderStatusCount() {
        OrderStatusVO orderStatusVO = orderService.orderStatusCount();
        return Result.success(orderStatusVO);
    }

    /**
     * 订单确认
     * @param orderConfirmDTO
     * @return
     */
    @PostMapping("confirm")
    @ApiOperation("订单确定")
    public Result<OrderConfirmVO> orderConfirm(@RequestBody OrderConfirmDTO orderConfirmDTO) {
        OrderConfirmVO orderConfirmVO = orderService.orderConfirm(orderConfirmDTO);
        return Result.success(orderConfirmVO);
    }
}

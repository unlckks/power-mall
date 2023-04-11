package com.mingyun.controller;

import com.mingyun.domain.Basket;
import com.mingyun.domain.Sku;
import com.mingyun.model.Result;
import com.mingyun.service.BasketService;
import com.mingyun.vo.CartMoneyVO;
import com.mingyun.vo.CartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:25
 */
@RestController
@Api(tags = "购物车管理接口")
@RequestMapping("p/shopCart")
public class BasketController {
    @Autowired
    private BasketService basketService;

    @GetMapping("prodCount")
    @ApiOperation("查询购物车数量")
    public Result<Integer> basketCount() {
        Integer count = basketService.basketCount();
        return Result.success(count);
    }

    @PostMapping("changeItem")
    @ApiOperation("添加或者修改购物车数量")
    public Result<String> changeBasket(@RequestBody Basket basket) {
        Integer i = basketService.changeBasket(basket);
        return Result.handle(i > 0);
    }

    @GetMapping("info")
    @ApiOperation("购物车详情")
    public Result<CartVO> cartInfo() {
        CartVO cartVO = basketService.cartInfo();
        return Result.success(cartVO);
    }

    @GetMapping("info")
    @ApiOperation("计算购物车选中的商品的价格")
    public Result<CartMoneyVO> calculateBasketMoney(@RequestBody List<Long> basketIds){
        CartMoneyVO cartMoneyVO = basketService.calculateBasketMoney(basketIds);
        return Result.success(cartMoneyVO);
    }
}

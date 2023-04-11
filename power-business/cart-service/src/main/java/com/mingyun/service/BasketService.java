package com.mingyun.service;

import com.mingyun.domain.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.vo.CartMoneyVO;
import com.mingyun.vo.CartVO;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-11 20:25
 */
public interface BasketService extends IService<Basket>{


        Integer basketCount();

        Integer changeBasket(Basket basket);

        CartMoneyVO calculateBasketMoney(List<Long> basketIds);

    CartVO cartInfo();

}

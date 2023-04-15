package com.mingyun.feign;

import com.mingyun.domain.Basket;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@FeignClient(value = "cart-service")
public interface OrderCartFeign {

    @GetMapping("p/shopCart/getBasketsByIds")
    Result<List<Basket>> getBasketsByIds(@RequestParam("basketIds") List<Long> basketIds);

}

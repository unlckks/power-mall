package com.mingyun.feign;

import com.mingyun.domain.ShopDetail;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@FeignClient(value = "store-service")
public interface OrderStoreFeign {
    @GetMapping("shop/getShopDetailsByIds")
    Result<List<ShopDetail>> getShopDetailsByIds(@RequestParam("shopIds") List<Long> shopIds);

}

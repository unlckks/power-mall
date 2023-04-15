package com.mingyun.feign;

import com.mingyun.domain.Sku;
import com.mingyun.model.ChangeStock;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@FeignClient(value = "product-service")
public interface OrderProdFeign {

    @GetMapping("prod/prod/getSkusByIds")
    Result<List<Sku>> getSkusByIds(@RequestParam("skuIds") List<Long> skuIds);

    @PostMapping("prod/prod/changeStocks")
    Result<Boolean> changeStocks(@RequestBody ChangeStock changeStock);
    @PostMapping("prod/prod/changeStocks")
    Result<Boolean> clearCart(List<Long> skuIds, String openId);
}

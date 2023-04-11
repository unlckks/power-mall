package com.mingyun.feign;

import com.mingyun.domain.Sku;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:42
 */
@FeignClient(value = "product-service")
public interface CartProdFeign {

    @GetMapping("prod/prod/getSkusByIds")
    Result<List<Sku>> getSkusByIds(@RequestParam("skuIds") List<Long> skuIds);

}

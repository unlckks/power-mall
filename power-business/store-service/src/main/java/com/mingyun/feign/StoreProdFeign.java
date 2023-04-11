package com.mingyun.feign;

import com.mingyun.domain.Prod;
import com.mingyun.feign.sentinel.StoreProdFeignSentinel;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@FeignClient(value = "product-service", fallback = StoreProdFeignSentinel.class)
public interface StoreProdFeign {

    @GetMapping("prod/prod/getProdById")
    Result<Prod> getProdById(@RequestParam("prodId") Long prodId);

}

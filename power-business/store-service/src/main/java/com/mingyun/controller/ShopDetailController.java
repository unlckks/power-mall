package com.mingyun.controller;

import com.mingyun.domain.ShopDetail;
import com.mingyun.model.Result;
import com.mingyun.service.ShopDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 19:44
 */
@RequestMapping("shop")
@RestController
@Api(tags = "店铺管理接口")
public class ShopDetailController {

    @Autowired
    private ShopDetailService shopDetailService;

    @GetMapping("getShopDetailsByIds")
    @ApiOperation("根据店铺的ids获取店铺集合")
    Result<List<ShopDetail>> getShopDetailsByIds(@RequestParam("shopIds") List<Long> shopIds) {
        List<ShopDetail> shopDetails = shopDetailService.listByIds(shopIds);
        return Result.success(shopDetails);
    }
}

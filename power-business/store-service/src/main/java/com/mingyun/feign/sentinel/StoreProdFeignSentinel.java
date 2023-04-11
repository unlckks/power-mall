package com.mingyun.feign.sentinel;

import com.mingyun.domain.Prod;
import com.mingyun.feign.StoreProdFeign;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@Component
@Slf4j
public class StoreProdFeignSentinel implements StoreProdFeign {

    @Override
    public Result<Prod> getProdById(Long prodId) {
        log.error("远程调用商品模块根据id查询商品失败:{}", prodId);
        return Result.fail(BusinessEnum.SERVER_INNER_ERROR);
    }
}

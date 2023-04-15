package com.mingyun.feign;

import com.mingyun.domain.MemberAddr;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:20
 */
@FeignClient(value = "member-service")
public interface OrderMemberFeign {


    @GetMapping("p/address/getDefaultAddr")
    Result<MemberAddr> getDefaultAddr(@RequestParam("openId")String openId);
}

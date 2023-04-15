package com.mingyun.feign;

import com.mingyun.domain.Member;
import com.mingyun.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 19:03
 */
@FeignClient(value = "member-service")
public interface ProdMemberFeign {


    @GetMapping("p/user/getMembersByOpenIds")
    Result<List<Member>> getMembersByOpenIds(@RequestParam("openIds")Set<String> openIds);


}

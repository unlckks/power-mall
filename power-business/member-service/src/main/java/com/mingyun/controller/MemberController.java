package com.mingyun.controller;

import com.mingyun.dto.MemberUpdateDTO;
import com.mingyun.model.Result;
import com.mingyun.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:58
 */
@RestController
@RequestMapping("p/user")
@Api(tags = "会员管理接口")
public class MemberController {
    @Autowired
    private MemberService memberService ;

    @PutMapping("setUserInfo")
    @ApiOperation("更新会员的信息")
    public Result<String> updateMember(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        Integer i = memberService.updateMember(memberUpdateDTO);
        return Result.handle(i > 0);
    }
}

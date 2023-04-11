package com.mingyun.controller;

import com.mingyun.domain.MemberAddr;
import com.mingyun.model.Result;
import com.mingyun.service.MemberAddrService;
import com.mingyun.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-10 14:31
 */
@RestController
@Api(tags = "会员的地址管理接口")
@RequestMapping("p/address")
public class MemberAddrController {


    @Autowired
    public MemberAddrService memberAddrService;

    /**
     * 获取地址
     * @return
     */
    @GetMapping("list")
    @ApiOperation("会员的收获地址")
    public Result<List<MemberAddr>> getMemberAddrs() {
        String openId = AuthUtil.getOpenId();
        List<MemberAddr> memberAddrs = memberAddrService.getMemberAddrs(openId);
        return Result.success(memberAddrs);
    }

    /**
     * 新增地址
     * @param memberAddr
     * @return
     */
    @PostMapping
    @ApiOperation("新增收获地址")
    public Result<String> addMemberAddrs(@RequestBody @Validated MemberAddr memberAddr) {
        String openId = AuthUtil.getOpenId();
        memberAddr.setOpenId(openId);
        Integer i = memberAddrService.addMemberAddrs(memberAddr);
        return Result.handle(i > 0);
    }


}

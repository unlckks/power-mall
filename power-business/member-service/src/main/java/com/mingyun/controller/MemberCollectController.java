package com.mingyun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.domain.MemberCollection;
import com.mingyun.model.Result;
import com.mingyun.service.MemberCollectionService;
import com.mingyun.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:58
 */
@RestController
@Api(tags = "会员收藏管理接口")
@RequestMapping("p/collection")
public class MemberCollectController {


    @Autowired
    private MemberCollectionService memberCollectionService;

    @GetMapping("count")
    @ApiOperation("查询会员的收藏数量")
    public Result<Long> getMemberCollectCount() {
        String openId = AuthUtil.getOpenId();
        long count = memberCollectionService.count(new LambdaQueryWrapper<MemberCollection>()
                .eq(MemberCollection::getOpenId, openId)
        );
        return Result.success(count);
    }

}

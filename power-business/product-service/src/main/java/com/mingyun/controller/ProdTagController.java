package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdTag;
import com.mingyun.dto.ProdTagQueryDTO;
import com.mingyun.model.Result;
import com.mingyun.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 18:50
 */
@RestController
@Api(tags = "活动标签管理接口")
@RequestMapping("prod/prodTag")
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    @GetMapping("page")
    @ApiOperation("分页查询活动标签")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    public Result<Page<ProdTag>> loadProdTagPage(PageDTO pageDTO, ProdTagQueryDTO prodTagDTO) {
        Page<ProdTag> prodTagPage = prodTagService.loadProdTagPage(pageDTO, prodTagDTO);
        return Result.success(prodTagPage);
    }

    @GetMapping("listTagList")
    @ApiOperation("查询可用的活动标签列表")
    public Result<List<ProdTag>> loadProdTags() {
        List<ProdTag> prodTags = prodTagService.loadProdTags();
        return Result.success(prodTags);
    }
}

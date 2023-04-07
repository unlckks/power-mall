package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.dto.ProdAddDTO;
import com.mingyun.dto.ProdQueryDTO;
import com.mingyun.model.Result;
import com.mingyun.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 19:56
 */
@RestController
@Api(tags = "商品管理")
@RequestMapping("prod/prod")
public class ProdController {
    @Autowired
    private ProdService prodService;

    /**
     * 进行分页
     * @param pageDTO
     * @param prodQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询活动标签")
    @PreAuthorize("hasAuthority('prod:prod:page')")
    public Result<Page<Prod>> loadProdPage(PageDTO pageDTO, ProdQueryDTO prodQueryDTO) {
        Page<Prod> prodPage = prodService.loadProdPage(pageDTO, prodQueryDTO);
        return Result.success(prodPage);
    }

    @PostMapping
    @ApiOperation("新增商品")
    @PreAuthorize("hasAuthority('prod:prod:save')")
    public Result<String> addProd(@RequestBody @Validated ProdAddDTO prodAddDTO) {
        Integer i = prodService.addProd(prodAddDTO);
        return Result.handle(i > 0);
    }
}

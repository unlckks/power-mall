package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdProp;
import com.mingyun.dto.PropAddDTO;
import com.mingyun.dto.PropQueryDTO;
import com.mingyun.model.Result;
import com.mingyun.service.ProdPropService;
import com.mingyun.vo.ProdPropVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 11:08
 */
@RestController
@Api(tags = "规格管理接口")
@RequestMapping("prod/spec")
public class ProdPropController {

    @Resource
    private ProdPropService prodPropService;

    @GetMapping("page")
    @ApiOperation("分页查询规格和规格值")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public Result<Page<ProdPropVo>> loadProdPropVoPage(PageDTO pageDTO, PropQueryDTO propQueryDTO) {
        Page<ProdPropVo> prodPropVoPage = prodPropService.loadProdProVoPage(pageDTO, propQueryDTO);
        return Result.success(prodPropVoPage);
    }

    /**
     * 进行新增
     * @param propAddDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增规格和规格值")
    @PreAuthorize("hasAuthority('prod:spec:save')")
    public Result<String> addPropAndValues(@RequestBody @Validated PropAddDTO propAddDTO) {
        Integer i = prodPropService.addPropAndValues(propAddDTO);
        return Result.handle(i > 0);
    }

}

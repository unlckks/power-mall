package com.mingyun.controller;

import com.mingyun.domain.Category;
import com.mingyun.model.Result;
import com.mingyun.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 09:55
 */
@RestController
@Api(tags = "分类管理接口")
@RequestMapping("prod/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 进行全查询
     *
     */
    @GetMapping("table")
    @ApiModelProperty("获取分类列表")
    public Result<List<Category>> loadCategory(){
        return Result.success(categoryService.loadCategorys());
    }

    @GetMapping("listCategory")
    @ApiOperation("查询可用所有父分类列表")
    @PreAuthorize("hasAuthority('prod:category:page')")
    public Result<List<Category>> loadParentCategorys() {
        return Result.success(categoryService.loadParentCategorys());
    }

    @PostMapping
    @ApiOperation("新增分类")
    @PreAuthorize("hasAuthority('prod:category:save')")
    public Result<String> addCategory(@RequestBody @Validated Category category) {
        Integer i = categoryService.addCategory(category);
        return Result.handle(i > 0);
    }

}

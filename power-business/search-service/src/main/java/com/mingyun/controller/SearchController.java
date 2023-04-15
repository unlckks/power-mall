package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.dto.PageDTO;
import com.mingyun.model.ProdEs;
import com.mingyun.model.Result;
import com.mingyun.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 18:54
 */
@RestController
@Api(tags = "搜索管理接口")
public class SearchController {

    @Autowired
    private SearchService searchService ;

    /**
     *    根据活动标签来搜索
     */
    @GetMapping("prod/prodListByTagId")
    @ApiOperation("根据标签来搜索商品")
    public Result<Page<ProdEs>> searchByTagId(Long tagId,  PageDTO pageDTO){
        Page<ProdEs> prodEsPage =searchService.searchEsByTagId(tagId ,pageDTO);
        return  Result.success(prodEsPage);
    }
    /**
     *
     *根据关键字查
     */
    @GetMapping("search/searchProdPage")
    @ApiOperation("根据关键字来搜索商品")
    public Result<Page<ProdEs>> searchByKeywords(Integer sort,PageDTO pageDTO , String prodName) {
        Page<ProdEs> prodEsPage = searchService.searchByKeywords(prodName, sort, pageDTO);
        return Result.success(prodEsPage);
    }

}

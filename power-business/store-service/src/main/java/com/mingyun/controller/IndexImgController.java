package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.IndexImg;
import com.mingyun.model.Result;
import com.mingyun.service.IndexImgService;
import com.mingyun.vo.IndexImgVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.statement.create.table.Index;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-07 19:39
 */
@RestController
@Api(tags = "轮播图管理接口")
@RequestMapping("admin/indexImg")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService ;

    /**
     * 分页查询轮播图
     * @param pageDTO
     * @param status
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询轮播图")
    @PreAuthorize("hasAuthority('admin:indexImg:page')")
    public Result<Page<IndexImg>> loadIndexImgPage(PageDTO pageDTO ,Integer status){
        Page<IndexImg> indexImgPage =indexImgService.loadIndexImgPage(pageDTO,status);
        return Result.success(indexImgPage);
    }

    /**
     * 新增轮播图
     * @param indexImg
     * @return
     */
    @PostMapping
    @ApiOperation("新增轮播图")
    @PreAuthorize("hasAuthority('admin:indexImg:save')")
    public Result<String> addIndexImg(@RequestBody @Validated IndexImg indexImg ){
        Integer i = indexImgService.addIndexImg(indexImg) ;
        return  Result.handle(i >0);
    }

    /**
     * 回显轮播图
     * @param id
     * @return
     */
    @GetMapping("info/{id}")
    @ApiOperation("回显轮播图")
    @PreAuthorize("hasAuthority('admin:indexImg:info')")
    public Result<IndexImgVO> findIndexImg (@PathVariable("id") Long id){
        IndexImgVO  indexImgVo = indexImgService.findIndexImg(id);
        return  Result.success(indexImgVo);
    }

    /**
     * wx 加载
     */
    @GetMapping("indexImgs")
    @ApiOperation("加载商城首页轮播图")
    public Result<List<IndexImg>> findMallIndexImg(){
        List<IndexImg> indexImgs =indexImgService.findMallIndexImg();
        return  Result.success(indexImgs);
    }
}

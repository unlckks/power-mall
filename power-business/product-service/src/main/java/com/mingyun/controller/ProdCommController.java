package com.mingyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.dto.PageDTO;
import com.mingyun.model.Result;
import com.mingyun.service.ProdCommService;
import com.mingyun.vo.ProdCommOverviewVO;
import com.mingyun.vo.ProdCommVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:12
 */
@RestController
@Api(tags = "评论管理接口")
@RequestMapping("prod/prodComm")
public class ProdCommController {

    @Autowired
    private ProdCommService prodCommService ;

    /**
     * 进行总评数
     */
    @GetMapping("prodComm/proCommData")
    @ApiOperation("查询商品的评论总览")
    public Result<ProdCommOverviewVO> prodCommOverview(Long prodId){
        ProdCommOverviewVO prodCommOverviewVO =prodCommService.prodCommOverView(prodId);
        return  Result.success(prodCommOverviewVO);
    }
    /**
     * 分页查询
     *处理细节 名字
     */
    @GetMapping("prodComm/prodCommPageByProd")
    @ApiOperation("分页查询商品的评论信息")
    public Result<Page<ProdCommVO>> prodCommMallPage(Long prodId, Integer evaluate, PageDTO pageDTO) {
        Page<ProdCommVO> prodCommVOPage = prodCommService.prodCommMallPage(prodId, evaluate, pageDTO);
        return Result.success(prodCommVOPage);
    }
}

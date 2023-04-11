package com.mingyun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.domain.Area;
import com.mingyun.model.Result;
import com.mingyun.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:32
 */
@RestController
@Api(tags = "地址管理接口")
@RequestMapping("admin/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @GetMapping("listByPid")
    @ApiOperation("省市区三级联动")
    public Result<List<Area>> listByPid(Long pid) {
        List<Area> areaList = areaService.list(new LambdaQueryWrapper<Area>()
                .eq(Area::getParentId, pid)
        );
        return Result.success(areaList);
    }
}

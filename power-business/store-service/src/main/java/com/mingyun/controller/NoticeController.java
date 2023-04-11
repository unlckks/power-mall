package com.mingyun.controller;

import com.mingyun.constant.NoticeConstant;
import com.mingyun.domain.Notice;
import com.mingyun.model.Result;
import com.mingyun.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:39
 */
@Api(tags = "公告管理")
@RestController
@RequestMapping("shop/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService ;

    /**
     * 查询商城首页的公告
     * @return
     */
    @GetMapping("topNoticeList")
    @ApiOperation("查询商城首页的公告")
    public Result<List<Notice>> findMallNotices(){
        List<Notice> notices = noticeService.findMallNotices();
        return Result.success(notices);
    }
}

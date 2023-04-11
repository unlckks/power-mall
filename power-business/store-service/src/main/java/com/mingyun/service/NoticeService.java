package com.mingyun.service;

import com.mingyun.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
public interface NoticeService extends IService<Notice>{

    /**
     * 查询商城首页的公告
     * @return
     */
        List<Notice> findMallNotices();
    }

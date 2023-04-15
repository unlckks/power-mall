package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.constant.NoticeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.Notice;
import com.mingyun.mapper.NoticeMapper;
import com.mingyun.service.NoticeService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.NoticeServiceImpl")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{
    @Autowired
    private  NoticeMapper noticeMapper ;

    @Override
    @Cacheable(key = NoticeConstant.NOTICE_MALL_KEY)
    public List<Notice> findMallNotices() {
    return     noticeMapper.selectList(new LambdaQueryWrapper<Notice>()
            .select(Notice::getTitle,Notice::getId)
            .eq(Notice::getStatus,1)
            .orderByDesc(Notice::getIsTop,Notice::getUpdateTime));
    }
}

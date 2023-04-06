package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.constant.ProdTagConstant;
import com.mingyun.dto.ProdTagQueryDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.ProdTag;
import com.mingyun.mapper.ProdTagMapper;
import com.mingyun.service.ProdTagService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.PageTagServiceImpl")
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService{


    @Resource
    private  ProdTagMapper prodTagMapper ;
    /**
     * 分页查询活动标签
     * @param pageDTO
     * @param prodTagDTO
     * @return
     */
    @Override
    public Page<ProdTag> loadProdTagPage(PageDTO pageDTO, ProdTagQueryDTO prodTagDTO) {
        Page<ProdTag> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        return prodTagMapper.selectPage(page,new LambdaQueryWrapper<ProdTag>()
                .eq(!ObjectUtils.isEmpty(prodTagDTO.getStatus()), ProdTag::getStatus,prodTagDTO.getStatus())
                .like(StringUtils.hasText(prodTagDTO.getTitle()),ProdTag::getTitle,prodTagDTO.getTitle())
                .orderByDesc(ProdTag::getSeq)
        );
    }

    /**
     * 查询可用的活动标签列表
     * @return
     */
    @Override
    @Cacheable(key = ProdTagConstant.PROD_TAG_LIST)
    public List<ProdTag> loadProdTags() {
        return prodTagMapper.selectList(new LambdaQueryWrapper<ProdTag>()
                .eq(ProdTag::getStatus, 1)
                .orderByDesc(ProdTag::getSeq)
        );
    }
}

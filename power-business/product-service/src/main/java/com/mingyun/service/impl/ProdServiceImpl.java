package com.mingyun.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdTagReference;
import com.mingyun.domain.Sku;
import com.mingyun.dto.ProdAddDTO;
import com.mingyun.dto.ProdQueryDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.mapper.ProdTagReferenceMapper;
import com.mingyun.mapper.SkuMapper;
import com.mingyun.model.DeliveryMode;
import com.mingyun.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.plaf.SliderUI;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.ProdMapper;
import com.mingyun.domain.Prod;
import com.mingyun.service.ProdService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.spring.web.json.Json;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 09:44
 */
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Resource
    private ProdMapper prodMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ProdTagReferenceMapper prodTagReferenceMapper;

    /**
     * 分页查询活动标签
     *
     * @param pageDTO
     * @param prodQueryDTO
     * @return
     */
    @Override
    public Page<Prod> loadProdPage(PageDTO pageDTO, ProdQueryDTO prodQueryDTO) {
        Page<Prod> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Long shopId = AuthUtil.getShopId();
        return prodMapper.selectPage(page, new LambdaQueryWrapper<Prod>()
                .eq(!shopId.equals(1L), Prod::getShopId, shopId)
                .eq(!ObjectUtils.isEmpty(prodQueryDTO.getStatus()), Prod::getStatus, prodQueryDTO.getStatus())
                .like(StringUtils.hasText(prodQueryDTO.getProdName()), Prod::getProdName, prodQueryDTO.getProdName())
                .orderByDesc(Prod::getUpdateTime)
        );
    }

    /**
     * 进行新增商品
     *
     * @param prodAddDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer addProd(ProdAddDTO prodAddDTO) {
        Long shopId = AuthUtil.getShopId();
        Long count = prodMapper.selectCount(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getShopId, shopId)
                .eq(Prod::getProdName, prodAddDTO.getProdName())
        );
        if (count > 0) {
            throw new BusinessException("该商品已经存在");
        }
        prodAddDTO.setShopId(shopId);
        prodAddDTO.setSoldNum(0);
        prodAddDTO.setCreateTime(new Date());
        prodAddDTO.setUpdateTime(new Date());
        if (prodAddDTO.getStatus().equals(1)) {
            prodAddDTO.setPutawayTime(new Date());
        }
        DeliveryMode deliveryModeVo = prodAddDTO.getDeliveryModeVo();
        prodAddDTO.setDeliveryMode(JSON.toJSONString(deliveryModeVo));
        int i = prodMapper.insert(prodAddDTO);
        handleSku(prodAddDTO);
        handleTagReference(prodAddDTO);
        return i;
    }

    /**
     * 进行处理标签
     *
     * @param prodAddDTO
     */
    private void handleTagReference(ProdAddDTO prodAddDTO) {
        List<Long> tagList = prodAddDTO.getTagList();
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        Long prodId = prodAddDTO.getProdId();
        tagList.forEach(tid -> {
            ProdTagReference prodTagReference = new ProdTagReference();
            prodTagReference.setTagId(tid);
            prodTagReference.setProdId(prodId);
            prodTagReference.setStatus(true);
            prodTagReference.setCreateTime(new Date());
            prodTagReferenceMapper.insert(prodTagReference);
        });

    }

    /**
     * 处理sku表
     *
     * @param prodAddDTO
     */
    private void handleSku(ProdAddDTO prodAddDTO) {
        List<Sku> skuList = prodAddDTO.getSkuList();
        if (CollectionUtils.isEmpty(skuList)) {
            return;
        }
        Long prodId = prodAddDTO.getProdId();
        skuList.forEach(sku -> {
            sku.setCreateTime(new Date());
            sku.setUpdateTime(new Date());
            sku.setProdId(prodId);
            skuMapper.insert(sku);
        });
    }
}

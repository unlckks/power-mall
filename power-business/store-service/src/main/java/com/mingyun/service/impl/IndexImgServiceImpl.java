package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.IndexImgConstant;
import com.mingyun.domain.Prod;
import com.mingyun.ex.BusinessException;
import com.mingyun.feign.StoreProdFeign;
import com.mingyun.model.Result;
import com.mingyun.utils.AuthUtil;
import com.mingyun.vo.IndexImgVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.IndexImgMapper;
import com.mingyun.domain.IndexImg;
import com.mingyun.service.IndexImgService;
import org.springframework.util.ObjectUtils;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.IndexImgServiceImpl")
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService{

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Autowired
    private StoreProdFeign storeProdFeign ;

    /**
     * 进行 分页查询轮播图
     * @param pageDTO
     * @param status
     * @return
     */
    @Override
    public Page<IndexImg> loadIndexImgPage(PageDTO pageDTO, Integer status) {
        Page<IndexImg> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Long shopId = AuthUtil.getShopId();
        return indexImgMapper.selectPage(page, new LambdaQueryWrapper<IndexImg>()
                .eq(!shopId.equals(1L),IndexImg::getImgId,shopId)
                .eq(!ObjectUtils.isEmpty(status),IndexImg::getStatus,status)
                .orderByDesc(IndexImg::getSeq));
    }

    /**
     * 轮播图
     * @param indexImg
     * @return
     */
    @Override
    public Integer addIndexImg(IndexImg indexImg) {
        Long shopId = AuthUtil.getShopId();
        indexImg.setShopId(shopId);
        indexImg.setCreateTime(new Date());
        return indexImgMapper.insert(indexImg);
    }

    /**
     * 回显轮播图
     * @param id
     * @return
     */
    @Override
    public IndexImgVO findIndexImg(Long id) {
        IndexImgVO indexImgVO = new IndexImgVO();
        IndexImg indexImg = indexImgMapper.selectById(id);
        BeanUtils.copyProperties(indexImg, indexImgVO);
        Long prodId = indexImg.getProdId();
        if (!ObjectUtils.isEmpty(prodId)) {
            // feign
            Result<Prod> result = storeProdFeign.getProdById(prodId);
            if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
                throw new BusinessException(result.getMsg());
            }
            Prod prod = result.getData();
            indexImgVO.setPic(prod.getPic());
            indexImgVO.setProdName(prod.getProdName());
        }
        return indexImgVO;
    }

    @Override
    @Cacheable(key = IndexImgConstant.INDEX_IMG_MALL_KEY)
    public List<IndexImg> findMallIndexImg() {
        return indexImgMapper.selectList(new LambdaQueryWrapper<IndexImg>()
                .select(IndexImg::getImgId,IndexImg::getProdId)
                .eq(IndexImg::getStatus,1)
                .orderByDesc(IndexImg::getSeq));
    }
}

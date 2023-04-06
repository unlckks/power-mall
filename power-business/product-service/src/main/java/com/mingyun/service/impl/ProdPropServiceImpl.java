package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdPropValue;
import com.mingyun.dto.PropAddDTO;
import com.mingyun.dto.PropQueryDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.mapper.ProdPropValueMapper;
import com.mingyun.utils.AuthUtil;
import com.mingyun.vo.ProdPropVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.ProdPropMapper;
import com.mingyun.domain.ProdProp;
import com.mingyun.service.ProdPropService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 09:44
 */
@Service
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService {

    @Autowired
    private ProdPropMapper prodPropMapper;

    @Autowired
    private ProdPropValueMapper prodPropValueMapper;

    /**
     * 分页查询规格和规格值
     *
     * @param pageDTO
     * @param propQueryDTO
     * @return
     */
    @Override
    public Page<ProdPropVo> loadProdProVoPage(PageDTO pageDTO, PropQueryDTO propQueryDTO) {
        Page<ProdProp> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Page<ProdPropVo> prodPropVoPage = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Long shopId = AuthUtil.getShopId();
        Page<ProdProp> prodPropPage = prodPropMapper.selectPage(page, new LambdaQueryWrapper<ProdProp>()
                .eq(!shopId.equals(1L), ProdProp::getShopId, shopId)
                .like(StringUtils.hasText(propQueryDTO.getPropName()), ProdProp::getPropName, propQueryDTO.getPropName())
        );
        List<ProdPropVo> prodPropList = prodPropVoPage.getRecords();
        if (CollectionUtils.isEmpty(prodPropList)) {
            return prodPropVoPage;
        }
        //获得规格值
        List<Long> propIds = prodPropList.stream()
                .map(ProdProp::getPropId)
                .collect(Collectors.toList());
        //直接在java中查询所以用in
        List<ProdPropValue> prodPropValues = prodPropValueMapper.selectList(new LambdaQueryWrapper<ProdPropValue>()
                .in(ProdPropValue::getPropId, propIds)
        );
        ArrayList<ProdPropVo> prodPropVos = new ArrayList<>(prodPropList.size());
        Map<Long, List<ProdPropValue>> map = prodPropValues.stream()
                .collect(Collectors.groupingBy(ProdPropValue::getPropId));
        prodPropList.forEach(prodProp -> {
            List<ProdPropValue> propValueList = prodPropValues.stream()
                    .filter(prodPropValue -> prodPropValue.getPropId().equals(prodProp.getPropId()))
                    .collect(Collectors.toList());
            List<ProdPropValue> prodValueList = map.get(prodProp.getPropId());
            ProdPropVo prodPropVo = new ProdPropVo();
            BeanUtils.copyProperties(prodProp, prodPropVo);
            prodPropVo.setProdPropValues(propValueList);
            prodPropVos.add(prodPropVo);
        });
        prodPropVoPage.setRecords(prodPropVos);
        prodPropVoPage.setTotal(prodPropPage.getTotal());
        return prodPropVoPage;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer addPropAndValues(PropAddDTO propAddDTO) {
        Long shopId = AuthUtil.getShopId();
        Long count = prodPropMapper.selectCount(new LambdaQueryWrapper<ProdProp>()
                .eq(ProdProp::getShopId, shopId)
                .eq(ProdProp::getPropName, propAddDTO.getPropName())
        );
        if (count > 0) {
            throw new BusinessException("该规则已经存在");
        }
        ProdProp prodProp = new ProdProp();
        prodProp.setPropName(propAddDTO.getPropName());
        prodProp.setShopId(shopId);
        int insert = prodPropMapper.insert(prodProp);
        List<ProdPropValue> prodPropValues = propAddDTO.getProdPropValues();
        if (!CollectionUtils.isEmpty(prodPropValues)) {
            prodPropValues.forEach(prodPropValue -> {
                prodPropValue.setPropId(prodProp.getPropId());
                prodPropValueMapper.insert(prodPropValue);
            });
        }
        return insert;
    }

}

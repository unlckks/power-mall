package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.constant.CategoryConstant;
import com.mingyun.ex.BusinessException;
import com.mingyun.mapper.ProdPropValueMapper;
import com.mingyun.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.CategoryMapper;
import com.mingyun.domain.Category;
import com.mingyun.service.CategoryService;
import org.springframework.util.ObjectUtils;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 09:44
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.CategoryServiceImpl")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 分页查询规格和规格值
     *
     * @return
     */
    @Override
    @Cacheable(key = CategoryConstant.CATEGORY_LIST)
    public List<Category> loadCategorys() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getSeq, Category::getUpdateTime)
        );
    }

    /**
     * 查询可用的父分离列表
     * @return
     */
    @Override
    @Cacheable(key = CategoryConstant.CATEGORY_PARENT_LIST)
    public List<Category> loadParentCategorys() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, 0L)
                .eq(Category::getStatus, 1)
                .orderByDesc(Category::getSeq, Category::getUpdateTime)
        );
    }

    /**
     * 进行新增分类
     *
     * @param category
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = CategoryConstant.CATEGORY_PARENT_LIST),
            @CacheEvict(key = CategoryConstant.CATEGORY_LIST)
    })
    public Integer addCategory(Category category) {
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, category.getCategoryName())
        );
        if (count > 0) {
            throw new BusinessException("该分类已经存在");
        }
        Long parentId = category.getParentId();
        if (ObjectUtils.isEmpty(parentId) || parentId.equals(0L)) {
            category.setParentId(0L);
        } else {
            //进行校验
            Category root = categoryMapper.selectById(parentId);
            if (ObjectUtils.isEmpty(root)) {
                throw new BusinessException("该父分类不存在");
            }
        }
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return categoryMapper.insert(category);
    }
}

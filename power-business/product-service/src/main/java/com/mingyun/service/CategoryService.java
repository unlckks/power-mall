package com.mingyun.service;

import com.mingyun.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.model.Result;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 09:44
 */
public interface CategoryService extends IService<Category> {


    List<Category> loadCategorys();

    List<Category> loadParentCategorys();

    Integer addCategory(Category category);
}

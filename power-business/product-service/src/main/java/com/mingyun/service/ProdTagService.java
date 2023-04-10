package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.ProdTagQueryDTO;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
public interface ProdTagService extends IService<ProdTag>{


        Page<ProdTag> loadProdTagPage(PageDTO pageDTO, ProdTagQueryDTO prodTagDTO);

    List<ProdTag> loadProdTags();

    List<ProdTag> prodTagMallList();

}

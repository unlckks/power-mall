package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.ProdAddDTO;
import com.mingyun.dto.ProdQueryDTO;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
public interface ProdService extends IService<Prod>{


        Page<Prod> loadProdPage(PageDTO pageDTO, ProdQueryDTO prodQueryDTO);

    Integer addProd(ProdAddDTO prodAddDTO);
}

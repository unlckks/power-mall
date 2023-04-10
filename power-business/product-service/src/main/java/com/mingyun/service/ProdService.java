package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.domain.Prod;
import com.mingyun.dto.PageDTO;
import com.mingyun.dto.ProdAddDTO;
import com.mingyun.dto.ProdQueryDTO;
import com.mingyun.vo.ProdSkuVO;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
public interface ProdService extends IService<Prod>{


    /**
     * 分页查询活动标签
     *
     * @param pageDTO
     * @param prodQueryDTO
     * @return
     */
    Page<Prod> loadProdPage(PageDTO pageDTO, ProdQueryDTO prodQueryDTO);

    /**
     * 新增商品
     *
     * @param prodAddDTO
     * @return
     */
    Integer addProd(ProdAddDTO prodAddDTO);

    /**
     * 根据商品id查询商品和sku集合信息
     *
     * @param prodId
     * @return
     */
    ProdSkuVO getProdAndSkus(Long prodId);
}

package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.domain.ProdPropValue;
import com.mingyun.dto.PropAddDTO;
import com.mingyun.dto.PropQueryDTO;
import com.mingyun.vo.ProdPropVo;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
public interface ProdPropService extends IService<ProdProp>{


        Page<ProdPropVo> loadProdProVoPage(PageDTO pageDTO, PropQueryDTO propQueryDTO);

    /**
     *添加
     * @param propAddDTO
     * @return
     */
    Integer addPropAndValues(PropAddDTO propAddDTO);

    List<ProdProp> loadProdProps(Long shopId);

    List<ProdPropValue> loadPropValues(Long propId);

}

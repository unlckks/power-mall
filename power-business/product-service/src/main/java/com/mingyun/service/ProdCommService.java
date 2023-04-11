package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.PageDTO;
import com.mingyun.vo.ProdCommOverviewVO;
import com.mingyun.vo.ProdCommVO;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
public interface ProdCommService extends IService<ProdComm>{

    /**
     * 进行总评数
     * @param prodId
     * @return
     */
        ProdCommOverviewVO prodCommOverView(Long prodId);

    /**
     * 分页查询
     * @param prodId
     * @param evaluate
     * @param pageDTO
     * @return
     */
    Page<ProdCommVO> prodCommMallPage(Long prodId, Integer evaluate, PageDTO pageDTO);

}

package com.mingyun.service;

import com.mingyun.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.vo.ProdCommOverviewVO;

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
    }

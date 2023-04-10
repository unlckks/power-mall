package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.model.CommOverview;
import com.mingyun.vo.ProdCommOverviewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.ProdComm;
import com.mingyun.mapper.ProdCommMapper;
import com.mingyun.service.ProdCommService;
import org.springframework.util.ObjectUtils;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService{

    @Autowired
    private ProdCommMapper prodCommMapper ;

    /**
     * 进行总评数
     * @param prodId
     * @return
     */
    @Override
    public ProdCommOverviewVO prodCommOverView(Long prodId) {
        ProdCommOverviewVO prodCommOverviewVO = new ProdCommOverviewVO();
        CommOverview commOverview = prodCommMapper.selectCommOverview(prodId);
        if(ObjectUtils.isEmpty(commOverview)||commOverview.getAllCount().equals(0L)){
            return prodCommOverviewVO;
        }
        BeanUtils.copyProperties(commOverview ,prodCommOverviewVO);
        if (! commOverview.getGoodCount().equals(0L)){
            BigDecimal goodLv = new BigDecimal(commOverview.getGoodCount().toString())
                    .divide(new BigDecimal(commOverview.getAllCount().toString()),
                            2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            prodCommOverviewVO.setGoodLv(goodLv);
        }
        Long picCount =prodCommMapper.selectCount(new LambdaQueryWrapper<ProdComm>()
                .eq(ProdComm::getProdId,prodId)
                .eq(ProdComm::getStatus,1)
                .isNotNull(ProdComm::getPics));
        prodCommOverviewVO.setPicCount(picCount);
        return prodCommOverviewVO ;
    }
}

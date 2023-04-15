package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.HttpConstant;
import com.mingyun.domain.Member;
import com.mingyun.dto.PageDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.feign.ProdMemberFeign;
import com.mingyun.model.CommOverview;
import com.mingyun.model.Result;
import com.mingyun.vo.ProdCommOverviewVO;
import com.mingyun.vo.ProdCommVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.ProdComm;
import com.mingyun.mapper.ProdCommMapper;
import com.mingyun.service.ProdCommService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService{

    @Autowired
    private ProdCommMapper prodCommMapper ;

    @Autowired
    private ProdMemberFeign prodMemberFeign;

    /**
     * 进行总评数
     * @param prodId
     * @return
     */
    @Override
    public ProdCommOverviewVO prodCommOverView(Long prodId) {
        ProdCommOverviewVO prodCommOverviewVO = new ProdCommOverviewVO();
        CommOverview commOverview = prodCommMapper.selectCommOverview(prodId);
        if (ObjectUtils.isEmpty(commOverview) || commOverview.getAllCount().equals(0L)) {
            return prodCommOverviewVO;
        }
        BeanUtils.copyProperties(commOverview, prodCommOverviewVO);
        if (!commOverview.getGoodCount().equals(0L)) {
            BigDecimal goodLv = new BigDecimal(commOverview.getGoodCount().toString())
                    .divide(new BigDecimal(commOverview.getAllCount().toString()),
                            2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            prodCommOverviewVO.setGoodLv(goodLv);
        }
        Long picCount = prodCommMapper.selectCount(new LambdaQueryWrapper<ProdComm>()
                .eq(ProdComm::getProdId, prodId)
                .eq(ProdComm::getStatus, 1)
                .isNotNull(ProdComm::getPics)
        );
        prodCommOverviewVO.setPicCount(picCount);
        return prodCommOverviewVO;
    }

    /**
     * 分页查询商品的评论信息
     * @param prodId
     * @param evaluate
     * @param pageDTO
     * @return
     */
    @Override
    public Page<ProdCommVO> prodCommMallPage(Long prodId, Integer evaluate, PageDTO pageDTO) {
        Page<ProdComm> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Page<ProdCommVO> prodCommVOPage = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        Page<ProdComm> prodCommPage = prodCommMapper.selectPage(page, new LambdaQueryWrapper<ProdComm>()
                .eq(ProdComm::getProdId, prodId)
                .eq(ProdComm::getStatus, 1)
                .eq(!evaluate.equals(-1) && !evaluate.equals(3), ProdComm::getEvaluate, evaluate)
                .isNotNull(evaluate.equals(3), ProdComm::getPics)
                .orderByDesc(ProdComm::getScore, ProdComm::getCreateTime)
        );
        List<ProdComm> prodCommList = prodCommPage.getRecords();
        if (CollectionUtils.isEmpty(prodCommList)) {
            return prodCommVOPage;
        }
        // 拿会员的信息
        Set<String> openIds = prodCommList.stream()
                .map(ProdComm::getOpenId)
                .collect(Collectors.toSet());
        // 远程调用
        Result<List<Member>> result = prodMemberFeign.getMembersByOpenIds(openIds);
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(result.getMsg());
        }
        List<Member> memberList = result.getData();
        Map<String, Member> memberMap = memberList.stream()
                .collect(Collectors.toMap(Member::getOpenId, m -> m));
        // 组装数据
        List<ProdCommVO> prodCommVOS = new ArrayList<>(prodCommList.size());
        prodCommList.forEach(prodComm -> {
            ProdCommVO prodCommVO = new ProdCommVO();
            BeanUtils.copyProperties(prodComm, prodCommVO);
            Member member = memberMap.get(prodComm.getOpenId());
            prodCommVO.setPic(member.getPic());
            // 昵称需要做一个转码
            String decode = null;
            try {
                decode = URLDecoder.decode(member.getNickName(), HttpConstant.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            prodCommVO.setNickName(decode);
            prodCommVOS.add(prodCommVO);
        });
        prodCommVOPage.setRecords(prodCommVOS);
        prodCommVOPage.setTotal(prodCommPage.getTotal());
        return prodCommVOPage;
    }

    }


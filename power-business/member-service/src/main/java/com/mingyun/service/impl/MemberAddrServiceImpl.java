package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.MemberAddr;
import com.mingyun.mapper.MemberAddrMapper;
import com.mingyun.service.MemberAddrService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
@Service
@CacheConfig(cacheNames = "com.mingyun.service.impl.MemberAddrServiceImpl")
public class MemberAddrServiceImpl extends ServiceImpl<MemberAddrMapper, MemberAddr> implements MemberAddrService{

    @Autowired
    private  MemberAddrMapper memberAddrMapper ;

    /**
     * 会员的收获地址
     * @param openId
     * @return
     */
    @Override
    @Cacheable(key = "#openId")
    public List<MemberAddr> getMemberAddrs(String openId) {
        return memberAddrMapper.selectList(new LambdaQueryWrapper<MemberAddr>()
                .eq(MemberAddr::getOpenId,openId)
                .eq(MemberAddr::getStatus,1)
                .orderByDesc(MemberAddr::getCommonAddr,MemberAddr::getUpdateTime));
    }

    @Override
    @CacheEvict(key = "#memberAddr.openId")
    public Integer addMemberAddrs(MemberAddr memberAddr) {
        Long count = memberAddrMapper.selectCount(new LambdaQueryWrapper<MemberAddr>()
                .eq(MemberAddr::getOpenId, memberAddr.getOpenId())
                .eq(MemberAddr::getCommonAddr, 1)
        );
        if (count <= 0L) {
            memberAddr.setCommonAddr(1);
        }
        memberAddr.setStatus(1);
        memberAddr.setCreateTime(new Date());
        memberAddr.setUpdateTime(new Date());
        return memberAddrMapper.insert(memberAddr);
    }
}

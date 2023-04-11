package com.mingyun.service;

import com.mingyun.domain.MemberAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
public interface MemberAddrService extends IService<MemberAddr>{

    /**
     * 获取地址
     * @param openId
     * @return
     */
        List<MemberAddr> getMemberAddrs(String openId);

    /**
     * 新增地址
     * @param memberAddr
     * @return
     */
    Integer addMemberAddrs(MemberAddr memberAddr);
}

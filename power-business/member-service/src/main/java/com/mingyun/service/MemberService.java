package com.mingyun.service;

import com.mingyun.domain.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.dto.MemberUpdateDTO;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
public interface MemberService extends IService<Member>{


        Integer updateMember(MemberUpdateDTO memberUpdateDTO);

    }

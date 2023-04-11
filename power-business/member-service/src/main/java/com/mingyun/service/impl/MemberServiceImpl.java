package com.mingyun.service.impl;

import com.mingyun.constant.HttpConstant;
import com.mingyun.dto.MemberUpdateDTO;
import com.mingyun.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.Member;
import com.mingyun.mapper.MemberMapper;
import com.mingyun.service.MemberService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService{
    @Autowired
    private MemberMapper memberMapper;
    @Override
    public Integer updateMember(MemberUpdateDTO memberUpdateDTO) {
        Long userId = AuthUtil.getUserId();
        Member member = new Member();
        member.setId(userId);
        member.setPic(memberUpdateDTO.getPic());
        // 乱码问题  转码 8F7SE8D7A8F%545Q&^KSAH 注意编码字符集 utf-8mb4
        String encode = null;
        try {
            encode = URLEncoder.encode(memberUpdateDTO.getNickName(), HttpConstant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        member.setNickName(encode);
        member.setUpdateTime(new Date());
        return memberMapper.updateById(member);
    }
}

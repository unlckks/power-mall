package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.MemberCollectionMapper;
import com.mingyun.domain.MemberCollection;
import com.mingyun.service.MemberCollectionService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-10 20:56
 */
@Service
public class MemberCollectionServiceImpl extends ServiceImpl<MemberCollectionMapper, MemberCollection> implements MemberCollectionService{

}

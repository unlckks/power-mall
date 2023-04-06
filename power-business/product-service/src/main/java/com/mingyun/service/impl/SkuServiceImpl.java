package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.SkuMapper;
import com.mingyun.domain.Sku;
import com.mingyun.service.SkuService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService{

}

package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.ProdMapper;
import com.mingyun.domain.Prod;
import com.mingyun.service.ProdService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{

}

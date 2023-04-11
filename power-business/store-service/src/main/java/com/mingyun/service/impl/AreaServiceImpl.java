package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.Area;
import com.mingyun.mapper.AreaMapper;
import com.mingyun.service.AreaService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{

}

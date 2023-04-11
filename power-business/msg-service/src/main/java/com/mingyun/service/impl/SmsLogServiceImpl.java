package com.mingyun.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.SmsLogMapper;
import com.mingyun.domain.SmsLog;
import com.mingyun.service.SmsLogService;
/**
 *  @Author: MingYun
 *  @Date: 2023-04-11 20:05
 */
@Service
public class SmsLogServiceImpl extends ServiceImpl<SmsLogMapper, SmsLog> implements SmsLogService{

}

package com.mingyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingyun.domain.ProdComm;
import com.mingyun.model.CommOverview;
import com.mingyun.model.CommStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Mapper
public interface ProdCommMapper extends BaseMapper<ProdComm> {
    /**
     * 进行查询数据库
     *
     * @param prodIds
     * @return
     */
    List<CommStatistics> selectCommStatistics(@Param("prodIds") List<Long> prodIds);

    CommOverview selectCommOverview(@Param("prodId") Long prodId);
}
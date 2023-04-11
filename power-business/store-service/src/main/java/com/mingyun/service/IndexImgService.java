package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.mingyun.domain.IndexImg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingyun.vo.IndexImgVO;

import java.util.List;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
public interface IndexImgService extends IService<IndexImg>{


        Page<IndexImg> loadIndexImgPage(PageDTO pageDTO, Integer status);

        Integer addIndexImg(IndexImg indexImg);

        IndexImgVO findIndexImg(Long id);

    List<IndexImg> findMallIndexImg();
}

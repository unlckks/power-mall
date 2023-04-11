package com.mingyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.dto.PageDTO;
import com.mingyun.model.ProdEs;
import org.springframework.stereotype.Service;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 18:54
 */

public interface SearchService {
    /**
     * 根据活动标签来搜索
     * @param tagId
     * @param pageDTO
     * @return
     */
    Page<ProdEs> searchEsByTagId(Long tagId, PageDTO pageDTO);

    /**
     * 根据关键字查
     * @param prodName
     * @param sort
     * @param pageDTO
     * @return
     */
    Page<ProdEs> searchByKeywords(String prodName, Integer sort, PageDTO pageDTO);
}

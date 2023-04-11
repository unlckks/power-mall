package com.mingyun.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingyun.constant.EsConstant;
import com.mingyun.dto.PageDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.model.ProdEs;
import com.mingyun.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 18:54
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 根据es进行搜索商品
     *
     * @param tagId
     * @param pageDTO
     * @return
     */
    @Override
    public Page<ProdEs> searchEsByTagId(Long tagId, PageDTO pageDTO) {
        //进行分页
        Page<ProdEs> prodEsPage = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        //设置es请求
        SearchRequest searchRequest = new SearchRequest(EsConstant.PROD_ES_INDEX);
        //创建es对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder query = QueryBuilders.matchQuery("tagList", tagId);
        searchSourceBuilder.query(query);
        searchSourceBuilder.from((pageDTO.getCurrent() - 1) * pageDTO.getSize());
        searchSourceBuilder.size(pageDTO.getSize());

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits searchHits = searchResponse.getHits();
        //进行总页数
        long totalCount = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        ArrayList<ProdEs> prodEsArrayList = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            String prodEsStr = hit.getSourceAsString();
            ProdEs prodEs = JSON.parseObject(prodEsStr, ProdEs.class);
            prodEsArrayList.add(prodEs);
        }
        prodEsPage.setTotal(totalCount);
        prodEsPage.setRecords(prodEsArrayList);
        return prodEsPage;
    }

    /**
     * 根据关键字搜索
     *
     * @param prodName
     * @param sort
     * @param pageDTO
     * @return
     */
    @Override
    public Page<ProdEs> searchByKeywords(String prodName, Integer sort, PageDTO pageDTO) {
        Page<ProdEs> prodEsPage = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        SearchRequest searchRequest = new SearchRequest(EsConstant.PROD_ES_INDEX);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder query = QueryBuilders.matchQuery("prodName", prodName);
        searchSourceBuilder.query(query);
        searchSourceBuilder.from((pageDTO.getCurrent() - 1) * pageDTO.getSize());
        searchSourceBuilder.size(pageDTO.getSize());
        searchSourceBuilder.sort(mySort(sort));
        HighlightBuilder high = new HighlightBuilder();
        high.field("prodName");
        high.preTags("<span style='color:red'>");
        high.postTags("</span>");
        searchSourceBuilder.highlighter(high);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits searchHits = searchResponse.getHits();
        long totalCount = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        ArrayList<ProdEs> prodEsArrayList = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            //取数据
            String prodEsStr = hit.getSourceAsString();
            ProdEs prodEs = JSON.parseObject(prodEsStr, ProdEs.class);
            //拿高亮数据
            HighlightField highlightField = hit.getHighlightFields().get("prodName");
          Text[] fragments = highlightField.fragments();
            StringBuffer sb =new StringBuffer();
            for (Text fragment : fragments) {
                sb.append(fragments.toString());
            }
          prodEs.setProdName(sb.toString());
            prodEsArrayList.add(prodEs);
        }
        prodEsPage.setTotal(totalCount);
        prodEsPage.setRecords(prodEsArrayList);
        return prodEsPage;
    }

    private SortBuilder<?> mySort(Integer sort) {

        switch (sort) {
            case 0:
                // 综合
                return SortBuilders.fieldSort("positiveRating").order(SortOrder.DESC);
            case 1:
                // 销量
                return SortBuilders.fieldSort("soldNum").order(SortOrder.DESC);
            case 2:
                // 价格
                return SortBuilders.fieldSort("price").order(SortOrder.ASC);
            default:
                throw new BusinessException("排序方式不允许");
        }
    }
}

package com.mingyun.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.config.EsImportConfig;
import com.mingyun.constant.EsConstant;
import com.mingyun.domain.Prod;
import com.mingyun.domain.ProdTagReference;
import com.mingyun.mapper.ProdCommMapper;
import com.mingyun.mapper.ProdMapper;
import com.mingyun.mapper.ProdTagReferenceMapper;
import com.mingyun.model.CommStatistics;
import com.mingyun.model.ProdEs;
import com.mingyun.service.ImportService;
import com.mingyun.uitls.EsImportThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


/**
 * @Author: MingYun
 * @Date: 2023-04-07 09:56
 */
@Service
@Slf4j
public class EsImportServiceImpl implements ImportService, CommandLineRunner {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private ProdTagReferenceMapper prodTagReferenceMapper;

    @Autowired
    private ProdCommMapper prodCommMapper;


    @Autowired
    private EsImportConfig esImportConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    int size = 20;

    /**
     * 进行全量（分量+多线程）
     */
    @Override

    public void importAll() {
        if (!esImportConfig.getFalg()) {
            log.info("已经导入过");
        }
        createProdEsIndex();
        Long totalCount = getTotalCount(null);
        if (totalCount < 0) {
            log.info("没有商品可用导入");
            return;
        }
        long totalPage = totalCount % this.size == 0 ? totalCount / this.size : ((totalCount / this.size) + 1);
        //mybatisplus 从1 开始
        for (int i = 1; i < totalPage; i++) {
            fetchProdToProdEs(i, this.size, null);
        }

    }

    /**
     * 进行查询数据库
     *
     * @param
     * @param size
     * @param
     * @param
     */
    private void fetchProdToProdEs(int current, int size, Date t) {
        List<Prod> prodList = prodMapper.selectMyPage((current - 1) * size, size, t);
        //进行 标签分组查询
        List<Long> prodIds = prodList.stream()
                .map(Prod::getProdId)
                .collect(Collectors.toList());
        List<ProdTagReference> prodTagReferences = prodTagReferenceMapper.selectList(new LambdaQueryWrapper<ProdTagReference>()
                .in(ProdTagReference::getProdId, prodIds)
        );
        Map<Long, List<ProdTagReference>> tagMap = prodTagReferences.stream()
                .collect(Collectors.groupingBy(ProdTagReference::getProdId));
        // 拿评论数据
        List<CommStatistics> commStatistics = prodCommMapper.selectCommStatistics(prodIds);
        Map<Long, CommStatistics> commMap = commStatistics.stream()
                .collect(Collectors.toMap(CommStatistics::getProdId, p -> p));
        //创建一个批处理请求
        BulkRequest bulkRequest = new BulkRequest(EsConstant.PROD_ES_INDEX);
        //进行组合数据
        prodList.forEach(prod -> {
            ProdEs prodEs = new ProdEs();
            //进行拷贝
            BeanUtils.copyProperties(prod, prodEs);
            List<ProdTagReference> tagReferences = tagMap.get(prod.getProdId());
            if (!CollectionUtils.isEmpty(tagReferences)) {
                List<Long> tagList = tagReferences.stream()
                        .map(ProdTagReference::getProdId)
                        .collect(Collectors.toList());
                prodEs.setTagList(tagList);
            }
            CommStatistics statistics = commMap.get(prod.getProdId());
            if (!ObjectUtils.isEmpty(statistics)) {
                Long allCount = statistics.getAllCount();
                Long goodCount = statistics.getGoodCount();
                if (!goodCount.equals(0L)) {
                    //进行计算
                    BigDecimal goodLV = new BigDecimal(goodCount.toString())
                            .divide(new BigDecimal(allCount.toString()), 2, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal("100"));
                    prodEs.setPraiseNumber(goodCount);
                    prodEs.setPositiveRating(goodLV);
                }
            }
            IndexRequest indexRequest = new IndexRequest(EsConstant.PROD_ES_INDEX);
            indexRequest.id(prodEs.getProdId().toString());
            indexRequest.source(JSON.toJSONString(prodEs), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = null;
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("---------第{}页导入完成，结果为:{}", current, !bulkResponse.hasFailures());

    }

    /**
     * 查总页数
     *
     * @param
     * @param
     * @return
     */
    private Long getTotalCount(Date t) {
        return prodMapper.selectCount(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getStatus, 1)
                .ge(t != null, Prod::getUpdateTime, t)
        );
    }

    /**
     * 创建索引
     */
    private void createProdEsIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(EsConstant.PROD_ES_INDEX);
        createIndexRequest.mapping("{\n" +
                "    \"properties\": {\n" +
                "        \"positiveRating\": {\n" +
                "            \"type\": \"double\"\n" +
                "        },\n" +
                "        \"tagList\": {\n" +
                "            \"type\": \"long\"\n" +
                "        },\n" +
                "        \"soldNum\": {\n" +
                "            \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"price\": {\n" +
                "            \"type\": \"double\"\n" +
                "        },\n" +
                "        \"prodName\": {\n" +
                "            \"analyzer\": \"ik_max_word\",\n" +
                "            \"type\": \"text\"\n" +
                "        },\n" +
                "        \"praiseNumber\": {\n" +
                "            \"type\": \"long\"\n" +
                "        },\n" +
                "        \"_class\": {\n" +
                "            \"index\": false,\n" +
                "            \"type\": \"keyword\",\n" +
                "            \"doc_values\": false\n" +
                "        },\n" +
                "        \"pic\": {\n" +
                "            \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"prodId\": {\n" +
                "            \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"categoryId\": {\n" +
                "            \"type\": \"long\"\n" +
                "        },\n" +
                "        \"shopId\": {\n" +
                "            \"type\": \"long\"\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);
        createIndexRequest.settings(Settings.builder()
                //根据数量决定
                .put("number_of_shards", 3)
                //导入es 关闭副本
                .put("number_of_replicas", 0)
                //关闭定时刷新
                .put("refresh_interval", "-1")
        );
        CreateIndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("创建商品索引:{}", indexResponse.isAcknowledged());
    }

    @Override
    @Scheduled(initialDelay = 120 * 100, fixedDelay = 120 * 1000)
    public void importUpdate() {
        //时间点
        Date t2 = new Date();
        log.info("增量开始");
        String t1str = redisTemplate.opsForValue().get(EsConstant.UPDATE_IMPORT_TIME_KEY);
        Date t1 = null;
        try {
            t1 = sdf.parse(t1str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long totalCount = getTotalCount(t1);
        if (totalCount <= 0L) {
            redisTemplate.opsForValue().set(EsConstant.UPDATE_IMPORT_TIME_KEY, sdf.format(t2));
            log.info("没有商品需要导入");
            return;
        }
        long totalPage = totalCount % this.size == 0 ? totalCount / this.size : ((totalCount / this.size) + 1);
        //使用countDownLath juc工具包
        CountDownLatch countDownLatch = new CountDownLatch((int) totalPage);
        for (int i = 0; i < totalPage; i++) {
            //进行异步
            int current = i;
            Date t = t1;
            EsImportThreadPool.esPool.execute(() -> {
                fetchProdToProdEs(current, this.size, t);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set(EsConstant.UPDATE_IMPORT_TIME_KEY, sdf.format(t2));
    }

    @Override
    public void run(String... args)  {
        importAll();
    }

}

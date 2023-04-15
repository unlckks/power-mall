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
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
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
 * @Date: 2023-04-13 19:04
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

    /**
     * 每页的条数 定义好 一般不建议超过2w
     */
    int size = 20;

    /**
     * 全量（分页+多线程）
     * 1.创建索引
     * 2.查总条数
     * 3.计算总页数 （totalPage = 总条数 % size == 0 ? 总条数 / size: ((总条数 / size)+1)）
     * 4.for(int i = 1;i <= totalPage;i++){
     * new Thread(()->{
     * prod: select(1,size);
     * prodEs{prod/tag/comm}
     * bulkRequest(prodEsList)
     * }).start();
     * }
     * --------------- 任务 ------------
     * case   when
     * 深分页如何优化？
     */
    @Override
    public void importAll() {
        if (!esImportConfig.getFlag()) {
            log.info("已经导入过了");
            return;
        }
        createProdEsIndex();
        Long totalCount = getTotalCount(null);
        if (totalCount <= 0L) {
            log.info("没有商品需要导入");
            return;
        }
        long totalPage = totalCount % this.size == 0 ? totalCount / this.size : ((totalCount / this.size) + 1);
        CountDownLatch countDownLatch = new CountDownLatch((int) totalPage);
        for (int i = 1; i <= totalPage; i++) {
            // 异步
            int current = i;
            EsImportThreadPool.esPool.execute(() -> {
                fetchProdToProdEs(current, this.size, null);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 手动刷新一次缓冲区数据
        RefreshRequest refreshRequest = new RefreshRequest(EsConstant.PROD_ES_INDEX);
        RefreshResponse refreshResponse = null;
        try {
            refreshResponse = restHighLevelClient.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新索引的设定
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(EsConstant.PROD_ES_INDEX);
        updateSettingsRequest.settings(Settings.builder()
                .put("number_of_replicas", 2)
                .put("refresh_interval", "1s")
        );
        AcknowledgedResponse acknowledgedResponse = null;
        try {
            acknowledgedResponse = restHighLevelClient.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("更新索引结果为:{}", acknowledgedResponse.isAcknowledged());
        Date t1 = new Date();
        redisTemplate.opsForValue().set(EsConstant.UPDATE_IMPORT_TIME_KEY, sdf.format(t1));
    }

    /**
     * 查询数据库
     * 转换商品信息
     * 导入es的方法
     * -----------------
     * 1.分页查询商品表
     * 2.拿到当前页商品对应的 标签数据
     * 3.拿到当前页商品对应的 评论数据
     * 4.组装prodEs
     * 5.导入es
     *
     * @param current
     * @param size
     * @param t
     */
    private void fetchProdToProdEs(int current, int size, Date t) {
        List<Prod> prodList = prodMapper.selectMyPage((current - 1) * size, size, t);
        // 查询 标签分组的数据
        List<Long> prodIds = prodList.stream()
                .map(Prod::getProdId)
                .collect(Collectors.toList());
        List<ProdTagReference> prodTagReferences = prodTagReferenceMapper.selectList(new LambdaQueryWrapper<ProdTagReference>()
                .in(ProdTagReference::getProdId, prodIds)
        );
        // prodId  List<Long>
        Map<Long, List<ProdTagReference>> tagMap = prodTagReferences.stream()
                .collect(Collectors.groupingBy(ProdTagReference::getProdId));
        // 拿评论数据
        // 不是要查500w条评论在内存中
        // prodId allCount goodCount
        // 81       3         1
        List<CommStatistics> commStatistics = prodCommMapper.selectCommStatistics(prodIds);
        Map<Long, CommStatistics> commMap = commStatistics.stream()
                .collect(Collectors.toMap(CommStatistics::getProdId, p -> p));
        // 创建一个批处理请求
        BulkRequest bulkRequest = new BulkRequest(EsConstant.PROD_ES_INDEX);
        //  组合数据
        prodList.forEach(prod -> {
            ProdEs prodEs = new ProdEs();
            BeanUtils.copyProperties(prod, prodEs);
            List<ProdTagReference> tagReferences = tagMap.get(prod.getProdId());
            if (!CollectionUtils.isEmpty(tagReferences)) {
                List<Long> tagList = tagReferences.stream()
                        .map(ProdTagReference::getTagId)
                        .collect(Collectors.toList());
                prodEs.setTagList(tagList);
            }
            CommStatistics statistics = commMap.get(prod.getProdId());
            if (!ObjectUtils.isEmpty(statistics)) {
                Long allCount = statistics.getAllCount();
                Long goodCount = statistics.getGoodCount();
                if (!goodCount.equals(0L)) {
                    // 计算
                    BigDecimal goodLV = new BigDecimal(goodCount.toString())
                            .divide(new BigDecimal(allCount.toString()), 2, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal("100"));
                    prodEs.setPraiseNumber(goodCount);
                    prodEs.setPositiveRating(goodLV);
                }
            }
            // prodEs 导入了 es的bulk
            IndexRequest indexRequest = new IndexRequest(EsConstant.PROD_ES_INDEX);
            indexRequest.id(prodEs.getProdId().toString());
            indexRequest.source(JSON.toJSONString(prodEs), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("=====第{}页导入完成，结果为:{}", current, !bulkResponse.hasFailures());

    }

    /**
     * 查询总条数
     *
     * @param t
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
                .put("number_of_shards", 3) // 根据数据量来决定的
                .put("number_of_replicas", 0) // 因为你导入es的时候 关闭副本的功能
                .put("refresh_interval", "-1") // 关闭定时刷新的操作
        );
        CreateIndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("创建商品索引:{}", indexResponse.isAcknowledged());
    }

    /**
     * 增量导入 执行n次（多线程+分页）
     * 并不是每次有写商品的操作 就执行导入
     * 搞一个定时任务 间隔5min执行一次
     * 如何确定哪些数据是增量数据？
     * updateTime
     * -----------
     * fixedDelay Execute the annotated method with a fixed period between the end of the last invocation and the start of the next
     * fixedRate  Execute the annotated method with a fixed period between invocations.
     */
    @Override
    @Scheduled(initialDelay = 120 * 1000, fixedRate = 120 * 1000)
    public void importUpdate() {
        // 进入就要给时间点
        Date t2 = new Date();
        log.info("增量开始");
        String t1Str = redisTemplate.opsForValue().get(EsConstant.UPDATE_IMPORT_TIME_KEY);
        Date t1 = null;
        try {
            t1 = sdf.parse(t1Str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long totalCount = getTotalCount(t1);
        if (totalCount <= 0L) {
            redisTemplate.opsForValue().set(EsConstant.UPDATE_IMPORT_TIME_KEY, sdf.format(t2));
            log.info("没有商品需要导入");
            return;
        }
        long totalPage = totalCount % this.size == 0 ? totalCount / this.size : ((totalCount / this.size) + 1);
        CountDownLatch countDownLatch = new CountDownLatch((int) totalPage);
        for (int i = 1; i <= totalPage; i++) {
            // 异步
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
    public void run(String... args) throws Exception {
        importAll();
    }
}


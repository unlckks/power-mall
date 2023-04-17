package com.mingyun.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.MqConstant;
import com.mingyun.domain.*;
import com.mingyun.dto.OrderConfirmDTO;
import com.mingyun.dto.OrderSubmitDTO;
import com.mingyun.ex.BusinessException;
import com.mingyun.feign.OrderCartFeign;
import com.mingyun.feign.OrderMemberFeign;
import com.mingyun.feign.OrderProdFeign;
import com.mingyun.feign.OrderStoreFeign;
import com.mingyun.mapper.OrderItemMapper;
import com.mingyun.model.*;
import com.mingyun.money.CalculateAmount;
import com.mingyun.utils.AuthUtil;
import com.mingyun.utils.MyIdUtil;
import com.mingyun.vo.OrderConfirmVO;
import com.mingyun.vo.OrderStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.mapper.OrderMapper;
import com.mingyun.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:00
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService, ApplicationContextAware {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderMemberFeign orderMemberFeign;

    @Autowired
    private OrderStoreFeign orderStoreFeign;

    @Autowired
    private OrderProdFeign orderProdFeign;

    private CalculateAmount calculateAmount;

    @Autowired
    private OrderCartFeign orderCartFeign;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private OrderItemMapper orderItemMapper ;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 进行查询订单状态
     *
     * @return
     */
    @Override
    public OrderStatusVO orderStatusCount() {
        OrderStatusVO orderStatusVO = new OrderStatusVO();
        String openId = AuthUtil.getOpenId();
        List<Order> orderList = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .select(Order::getStatus)
                .eq(Order::getOrderId, openId)
                .eq(Order::getDeleteStatus, 0));
        if (!CollectionUtils.isEmpty(orderList)) {
            long unPay = orderList.stream().filter(order -> order.getStatus().equals(1)).count();
            long payed = orderList.stream().filter(order -> order.getStatus().equals(2)).count();
            long consignment = orderList.stream().filter(order -> order.getStatus().equals(3)).count();
            orderStatusVO.setConsignment(consignment);
            orderStatusVO.setPayed(payed);
            orderStatusVO.setUnPay(unPay);
        }
        return orderStatusVO;
    }

    /**
     * 订单确认
     *
     * @param orderConfirmDTO
     * @return
     */
    @Override
    public OrderConfirmVO orderConfirm(OrderConfirmDTO orderConfirmDTO) {
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        //取收货的地址
        String openId = AuthUtil.getOpenId();
        Result<MemberAddr> addrResult = orderMemberFeign.getDefaultAddr(openId);
        MemberAddr memberAddr = addrResult.getData();
        orderConfirmVO.setMemberAddr(memberAddr);
        //判断入口
        List<Long> basketIds = orderConfirmDTO.getBasketIds();
        List<SkuBuyModel> skuBuyModels = new ArrayList<>();
        if (CollectionUtils.isEmpty(basketIds)) {
            //单品确认
            prod2Confirm(orderConfirmVO, orderConfirmDTO.getOrderItem(), skuBuyModels);
        } else {
            cart2Confirm(orderConfirmVO, basketIds, skuBuyModels);
        }
        return null;
    }


    /**
     * 单品确认
     *
     * @param orderConfirmVO
     * @param skuBuyModels
     */
    private void prod2Confirm(OrderConfirmVO orderConfirmVO, ProdConfirmModel prodConfirmModel, List<SkuBuyModel> skuBuyModels) {
        //取店铺
        Result<List<ShopDetail>> shopResult = orderStoreFeign.getShopDetailsByIds(Arrays.asList(prodConfirmModel.getShopId()));
        if (shopResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(shopResult.getMsg());
        }
        ShopDetail shopDetail = shopResult.getData().get(0);
        //取商品
        Result<List<Sku>> skuResult = orderProdFeign.getSkusByIds(Arrays.asList(prodConfirmModel.getSkuId()));
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(shopResult.getMsg());
        }
        Sku sku = skuResult.getData().get(0);
        //组装数据
        orderConfirmVO.setTotalCount(prodConfirmModel.getProdCount());
        //创建一个店铺集合
        ArrayList<ShopOrder> shopOrders = new ArrayList<>(1);
        //创建一个店铺对象
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setShopId(prodConfirmModel.getShopId());
        shopOrder.setShopLink(shopDetail.getShopAddress());
        shopOrder.setShopLogo(shopDetail.getShopLogo());
        shopOrder.setShopName(shopDetail.getShopName());
        //创建一个商品集合
        ArrayList<OrderItem> orderItems = new ArrayList<>(1);
        //创建 一个商品对象
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(prodConfirmModel, orderItem);
        BeanUtils.copyProperties(sku, orderItem);
        orderItems.add(orderItem);
        shopOrder.setShopOrderItems(orderItems);
        shopOrders.add(shopOrder);
        orderConfirmVO.setShopOrders(shopOrders);
        //进行计算
        SkuBuyModel skuBuyModel = new SkuBuyModel();
        skuBuyModel.setSkuId(prodConfirmModel.getSkuId());
        skuBuyModel.setPrice(sku.getPrice());
        skuBuyModel.setCount(prodConfirmModel.getProdCount());
        skuBuyModels.add(skuBuyModel);


    }

    /**
     * 购物车确认
     *
     * @param orderConfirmVO
     * @param basketIds
     * @param skuBuyModels
     */
    private void cart2Confirm(OrderConfirmVO orderConfirmVO, List<Long> basketIds, List<SkuBuyModel> skuBuyModels) {
        //取购物车
        Result<List<Basket>> basketResult = orderCartFeign.getBasketsByIds(basketIds);
        if (basketResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(basketResult.getMsg());
        }
        List<Basket> basketList = basketResult.getData();
        List<Long> shopIds = basketList.stream().map(Basket::getShopId).collect(Collectors.toList());
        List<Long> skuIds = basketList.stream().map(Basket::getSkuId).collect(Collectors.toList());
        //取店铺
        Result<List<ShopDetail>> shopResult = orderStoreFeign.getShopDetailsByIds(shopIds);
        if (shopResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(shopResult.getMsg());
        }
        List<ShopDetail> shopDetailList = shopResult.getData();
        Map<Long, ShopDetail> shopDetailMap = shopDetailList.stream().collect(Collectors.toMap(ShopDetail::getShopId, shop -> shop));
        //取商品
        Result<List<Sku>> skuResult = orderProdFeign.getSkusByIds(skuIds);
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException(shopResult.getMsg());
        }
        List<Sku> skuList = skuResult.getData();
        Map<Long, Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getSkuId, sku -> sku));
        //进行组装
        Integer totalCount = basketList.stream().map(Basket::getProdCount).reduce(Integer::sum).get();
        orderConfirmVO.setTotalCount(totalCount);
        //创建店铺的集合
        ArrayList<ShopOrder> shopOrders = new ArrayList<>();
        basketList.forEach(basket -> {
            Long shopId = basket.getShopId();
            List<ShopOrder> shopOrderList = shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getShopId()
                            .equals(shopId)).collect(Collectors.toList());
            List<OrderItem> orderItems = null;
            OrderItem orderItem = new OrderItem();
            if (CollectionUtils.isEmpty(shopOrderList)) {
                ShopOrder shopOrder = new ShopOrder();
                shopOrder.setShopId(shopId);
                ShopDetail shopDetail = shopDetailMap.get(shopId);
                shopOrder.setShopLogo(shopDetail.getShopLogo());
                shopOrder.setShopName(shopDetail.getShopName());
                shopOrder.setShopLink(shopDetail.getShopAddress());
                //创建一个集合
                orderItems = new ArrayList<>();
                shopOrder.setShopOrderItems(orderItems);
                shopOrders.add(shopOrder);
            } else {
                //获取店铺
                ShopOrder shopOrder = shopOrderList.get(0);
                orderItems = shopOrder.getShopOrderItems();
            }
            BeanUtils.copyProperties(basket, orderItem);
            Sku sku = skuMap.get(basket.getSkuId());
            BeanUtils.copyProperties(sku, orderItem);
            orderItems.add(orderItem);
            //进行计算
            SkuBuyModel skuBuyModel = new SkuBuyModel();
            skuBuyModel.setCount(basket.getProdCount());
            skuBuyModel.setSkuId(basket.getSkuId());
            skuBuyModel.setPrice(sku.getPrice());
            skuBuyModels.add(skuBuyModel);
        });
        orderConfirmVO.setShopOrders(shopOrders);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, CalculateAmount> moneyHandler = applicationContext.getBeansOfType(CalculateAmount.class);
        CalculateAmount prodMoneyHandler = moneyHandler.get("prodMoneyHandler");
        CalculateAmount actualMoneyHandler = moneyHandler.get("actualMoneyHandler");
        CalculateAmount couponMoneyHandler = moneyHandler.get("couponMoneyHandler");
        CalculateAmount reduceMoneyHandler = moneyHandler.get("reduceMoneyHandler");
        CalculateAmount transMoneyHandler = moneyHandler.get("transMoneyHandler");
        //排序
        prodMoneyHandler.setNextHandler(reduceMoneyHandler);
        reduceMoneyHandler.setNextHandler(couponMoneyHandler);
        couponMoneyHandler.setNextHandler(transMoneyHandler);
        transMoneyHandler.setNextHandler(actualMoneyHandler);
        this.calculateAmount = prodMoneyHandler;
    }

    /**
     * 进行下单
     *1.生成订单号
     * 2.清空购物车
     * 3.扣减库存
     * 4.写订单表
     * 5.写延迟消息
     * 对接支付
     * 问题：分布式事务问题
     * @param orderSubmitDTO
     * @return
     */
    @Override

    public Map<String, String> orderSubmit(OrderSubmitDTO orderSubmitDTO) {
        //进行初始化加载
        OrderSubmitContext orderSubmitContext =   createOrderSubmitContext(orderSubmitDTO);
        //进行清空购物车
        clearCart(orderSubmitContext);
        //扣减库存
        changeStock(orderSubmitContext);
        //写订单表
        writeOrder(orderSubmitContext);
        //写延迟消
        writeMsQueue(orderSubmitContext);
        log.info("--------------下单成功---------------");
        return null;
    }

    /**
     * mq 延迟消息
     * @param orderSubmitContext
     */
    private void writeMsQueue(OrderSubmitContext orderSubmitContext) {
        String orderSn =orderSubmitContext.getOrderSn() ;
        ChangeStock changeStock =orderSubmitContext.getChangeStock();
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(changeStock))
                .setHeader(RocketMQHeaders.KEYS, orderSn)
                .build();
        SendResult sendResult = rocketMQTemplate.syncSend(MqConstant.ORDER_MS_TOPIC, message, 6000, 5);
        log.info("发送订单延迟消息，订单号为:{},发送结果为:{}", orderSn, sendResult.getSendStatus());

    }

    /**
     * 写订单方法
     *
     * @param orderSubmitContext
     */
    private void writeOrder(OrderSubmitContext orderSubmitContext) {
        //skuIds 远程调用
        List<Long> skuIds = orderSubmitContext.getSkuIds();
        Result<List<Sku>> skuResult = orderProdFeign.getSkusByIds(skuIds);
        if (skuResult.getData().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw  new BusinessException(skuResult.getMsg());
        }
        List<Sku> skuList =skuResult.getData();
        Map<Long,Sku> skuMap =skuList.stream().collect(Collectors.toMap(Sku::getSkuId ,sku ->sku));
        ArrayList<SkuBuyModel> skuBuyModels =new ArrayList<>();
        ArrayList<Integer> oneCounts =new ArrayList<>() ;
        //写 orderItem表
        OrderSubmitDTO orderSubmitDTO  = orderSubmitContext.getOrderSubmitDTO();
        String  orderSn =orderSubmitContext.getOrderSn();
        String openId =orderSubmitContext.getOpenId();
        orderSubmitDTO.getShopOrders().forEach(shopOrder -> {
            shopOrder.getShopOrderItems().forEach(orderItem -> {
                orderItem.setCreateTime(new Date());
                orderItem.setCommSts(0);
                orderItem.setOrderNumber(orderSn);
                //进行金额数据的处理
                Sku sku =skuMap.get(orderItem.getSkuId());
                BigDecimal price = sku.getPrice();
                Integer prodCount =orderItem.getProdCount();
                Long skuId =orderItem.getSkuId();
                orderItem.setPrice(sku.getPrice());
                //计算单品总价
                BigDecimal oneMoney =price.multiply(new BigDecimal(prodCount.toString()));
                orderItem.setProductTotalAmount(oneMoney);
                //进行写orderItem
                orderItemMapper.insert(orderItem);
                skuBuyModels.add(new SkuBuyModel(skuId,price,prodCount));
                oneCounts.add(prodCount);
            });
        });
        //计算费用
        OrderMoneyContext orderMoneyContext =new OrderMoneyContext( );
        orderMoneyContext.setSkuBuyModels(skuBuyModels);
        orderMoneyContext.setOpenId(openId);
        calculateAmount.calculate(orderMoneyContext);
        //按需求设置金额
        Order order = new Order();
        order.setOpenId(openId);
        order.setOrderNumber(orderSn);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setRemarks(orderSubmitDTO.getRemarks());
        order.setIsPayed(false);
        order.setStatus(1);
        MemberAddr memberAddr =orderSubmitDTO.getMemberAddr();
        StringBuilder sb = new StringBuilder();
        String memberAddrDetails  =sb.append(memberAddr.getProvince())
                .append(memberAddr.getCity())
                .append(memberAddr.getArea())
                .append(memberAddr.getAddr())
                .toString();
        //收货地址
        order.setAddrOrderDetails(memberAddrDetails);
        Integer totalCount =oneCounts.stream().reduce(Integer::sum).get();
        order.setProductNums(totalCount);
        //设置费用
        //实付
        order.setActualTotal(orderMoneyContext.getActualTotal());
        //运费
        order.setFreightAmount(orderMoneyContext.getTransfee());
        //折扣
        order.setReduceAmount(orderMoneyContext.getShopReduce());
        //商品 金额
        order.setTotalMoney(orderMoneyContext.getTotalMoney());
        orderMapper.insert(order);
    }

    /**
     * 扣减库存
     * @param orderSubmitContext
     */
    private void changeStock(OrderSubmitContext orderSubmitContext) {
        ChangeStock changeStock = orderSubmitContext.getChangeStock();
        Result<Boolean> result  =orderProdFeign.changeStocks(changeStock);
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())|| !result.getData()){
            //扣减失败
            throw  new BusinessException(result.getMsg());
        }
    }

    /**
     * 清空购物车
     * @param orderSubmitContext
     */
    private void clearCart(OrderSubmitContext orderSubmitContext) {
        OrderSubmitDTO orderSubmitDTO = orderSubmitContext.getOrderSubmitDTO();
        List<Long> skuIds =orderSubmitContext.getSkuIds();
        String openId  =orderSubmitContext.getOpenId();
        if (orderSubmitDTO.getOrderEntry().equals(0)){
            //进行远程调用 清空购物车
            Result<Boolean> result =orderProdFeign.clearCart(skuIds ,openId);
            if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode()) || !result.getData()){
                    //清空失败
                throw  new BusinessException(result.getMsg());
            }
        }
    }

    /**
     * 进行初始化加载
     * @param orderSubmitDTO
     */
    private OrderSubmitContext createOrderSubmitContext(OrderSubmitDTO orderSubmitDTO) {
        OrderSubmitContext orderSubmitContext = new OrderSubmitContext();
        String openId =AuthUtil.getOpenId();
        //生成订单号 雪花算法
       String orderSn = createOrderSn();
        ArrayList<Long> skuIds = new ArrayList<>();
        //需要skuIds
        List<SkuStock> skuStocks =new ArrayList<>();
        List<ProdStock> prodStocks =new ArrayList<>();
        orderSubmitDTO.getShopOrders().forEach(shopOrder -> {
            shopOrder.getShopOrderItems().forEach(orderItem -> {
                Long skuId =orderItem.getSkuId();
                skuIds.add(skuId);
                 Long prodId =orderItem.getProdId();
                 //进行判断 通过+-
              Integer prodCount =  orderItem.getProdCount()*-1;
              String prodName =orderItem.getProdName();
              SkuStock skuStock =new SkuStock( );
                skuStock.setSkuId(skuId);
                skuStock.setCount(prodCount);
                skuStock.setProdName(prodName);
                skuStocks.add(skuStock);
                    List<ProdStock> prodStockList =prodStocks.stream()
                            .filter(prodStock -> prodStock.getProdId().equals(prodId))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(prodStockList)){
                        ProdStock prodStock= new ProdStock() ;
                        prodStock.setProdId(prodId);
                        prodStock.setCount(prodCount);
                        prodStocks.add(prodStock);
                    }else{
                        ProdStock prodStock = prodStockList.get(0);
                        prodStock.setCount(prodStock.getCount()+prodCount);
                    }
            });
        });
        //远程调用
        ChangeStock changeStock =new ChangeStock(skuStocks ,prodStocks);
        orderSubmitContext.setSkuIds(skuIds);
        orderSubmitContext.setChangeStock(changeStock);
        orderSubmitContext.setOpenId(openId);
        orderSubmitContext.setOrderSn(orderSn);
        orderSubmitContext.setOrderSubmitDTO(orderSubmitDTO);
        return orderSubmitContext;
    }

    private String createOrderSn() {
        return snowflake.nextIdStr();
    }

    /**
     * 进行回滚
     *下单未成功，将订单还原
     * @param order
     * @param changeStock
     */
    @Override
    @Transactional
    public void realDoBack(Order order, ChangeStock changeStock) {
        order.setStatus(6);
        order.setCloseType(1);

        order.setUpdateTime(new Date());
        int i =orderMapper.updateById(order);
        if (i <= 0) {
            log.error("回滚修改订单状态失败，订单编号:{}", order.getOrderNumber());
            throw new RuntimeException("回滚修改订单状态失败");
        }
        // 回滚库存
        changeStock.getSkuStocks().stream().forEach(skuStock -> skuStock.setCount(skuStock.getCount() * -1));
        changeStock.getProdStocks().stream().forEach(prodStock -> prodStock.setCount(prodStock.getCount() * -1));
        // 远程调用
        Result<Boolean> result = orderProdFeign.changeStocks(changeStock);
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode()) || !result.getData()) {
            log.error("回滚库存失败，订单编号:{}", order.getOrderNumber());
            throw new RuntimeException("回滚库存失败");
        }

    }
}

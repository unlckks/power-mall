package com.mingyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.domain.ShopDetail;
import com.mingyun.domain.Sku;
import com.mingyun.ex.BusinessException;
import com.mingyun.feign.CartProdFeign;
import com.mingyun.feign.CartStoreFeign;
import com.mingyun.model.CartItem;
import com.mingyun.model.Result;
import com.mingyun.model.ShopCart;
import com.mingyun.utils.AuthUtil;
import com.mingyun.vo.CartMoneyVO;
import com.mingyun.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingyun.domain.Basket;
import com.mingyun.mapper.BasketMapper;
import com.mingyun.service.BasketService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-11 20:25
 */
@Service
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService{

    @Autowired
    private BasketMapper basketMapper ;

    @Autowired
    private CartProdFeign cartProdFeign ;

    @Autowired
     private CartStoreFeign CartStoreFeign ;

    /**
     * 查询购物车数量
     * @return
     */
    @Override
    public Integer basketCount() {
        String openId = AuthUtil.getOpenId() ;
        List<Object> objs = basketMapper.selectObjs(new QueryWrapper<Basket>()
                .select("IFNULL(sum(prod_count),0)")
                .eq("open_id",openId));
        Object o =  objs.get(0);
        return Integer.parseInt(o.toString());
    }

    /**
     * 添加购物车或修改
     * @param basket
     * @return
     */
    @Override
    public Integer changeBasket(Basket basket) {
        Integer allCount = basketCount();
        int finalCount = allCount  + basket.getProdCount();
        if (finalCount > 100) {
            throw  new BusinessException("购物车商品达到上项");
        }
        String openId =AuthUtil.getOpenId() ;
        Basket oldBasket = basketMapper.selectOne( new LambdaQueryWrapper<Basket>()
                .eq(Basket::getOpenId, openId)
                .eq(Basket::getSkuId, basket.getSkuId()));
        if (ObjectUtils.isEmpty(oldBasket)){
            //进行新增
            basket.setCreateTime(new Date());
            basket.setOpenId(openId);
            return basketMapper.insert(basket);
        }
        int prodCount = oldBasket.getProdCount() + basket.getProdCount();
        if (prodCount<=0){
            throw new BusinessException("购物车商品数量不能小于0");
        }
        oldBasket.setProdCount(prodCount);
        oldBasket.setCreateTime(new Date());
        return basketMapper.updateById(oldBasket);
    }
    @Override
    public CartVO cartInfo() {
        CartVO cartVO = new CartVO();
        String openId = AuthUtil.getOpenId();
      List<Basket> basketList =  basketMapper.selectList(new LambdaQueryWrapper<Basket>()
              .eq(Basket::getOpenId,openId)
              .orderByDesc(Basket::getCreateTime)
      );
      if (CollectionUtils.isEmpty(basketList)){
          return  cartVO ;
      }
      //取商品
        List<Long> skuIds = basketList.stream().map(Basket::getSkuId).collect(Collectors.toList());
        Result<List<Sku>> skuResult = cartProdFeign.getSkusByIds(skuIds);
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw  new BusinessException(skuResult.getMsg());
        }
        List<Sku> skuList =skuResult.getData() ;
        Map<Long, Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getSkuId, sku -> sku));
        //取店铺
        List<Long> shopIds = basketList.stream().map(Basket::getShopId).collect(Collectors.toList());
        Result<List<ShopDetail>> shopResult = CartStoreFeign.getShopDetailsByIds(shopIds);
        if (shopResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw  new BusinessException(shopResult.getMsg());
        }
        List<ShopDetail> shopDetailList = shopResult.getData() ;
        Map<Long,ShopDetail> shopDetailMap =shopDetailList.stream()
                .collect(Collectors.toMap(ShopDetail::getShopId,shop->shop));
        //按店铺来分组
        List<ShopCart> shopCarts =new ArrayList<>( );
        basketList.forEach(basket -> {
            Long shopId =basket.getShopId() ;
            List<ShopCart> shopCartList =shopCarts.stream()
                    .filter(shopCart -> shopCart.getShopId().equals(shopId))
                    .collect(Collectors.toList());
            CartItem cartItem =new CartItem( );
            ShopCart shopCart =null ;
            List<CartItem> cartItems = null ;
            if (CollectionUtils.isEmpty(shopCartList)){
                shopCart = new ShopCart() ;
                shopCart.setShopId(shopId );
                //发现对应的店铺
                ShopDetail shopDetail = shopDetailMap.get(shopId);
                shopCart.setShopName(shopDetail.getShopName());
                shopCart.setShopLogo(shopDetail.getShopLogo());
                shopCart.setShopLink(shopDetail.getShopAddress());
                //创建集合
                cartItems = new ArrayList<>( );
                shopCart.setShopCartItems(cartItems);
                shopCarts.add(shopCart);
            }else {
                //取出 店铺
                shopCart = shopCartList.get(0);
                cartItems =shopCart.getShopCartItems();
            }
            BeanUtils.copyProperties(basket ,cartItem);
            Sku sku  =  skuMap.get(basket.getSkuId());
            BeanUtils.copyProperties(sku,cartItem);
            });
        cartVO.setShopCarts(shopCarts);
        return cartVO ;

    }
    @Override
    public CartMoneyVO calculateBasketMoney(List<Long> basketIds) {
        CartMoneyVO cartMoneyVO =new CartMoneyVO( );
        if (CollectionUtils.isEmpty(basketIds)){
            return  cartMoneyVO ;
        }
        List<Basket> baskets= basketMapper.selectBatchIds(basketIds);
        List<Long> skuIds = baskets.stream().map(Basket::getSkuId).collect(Collectors.toList());
        Result<List<Sku>> skuResult =cartProdFeign.getSkusByIds(skuIds);
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw  new BusinessException(skuResult.getMsg());
        }
        List<Sku> skuList =skuResult.getData() ;
        Map<Long, BigDecimal> priceMap =skuList.stream()
                .collect(Collectors.toMap(Sku::getSkuId,Sku::getPrice));
        List<BigDecimal> oneMoneys =new ArrayList<>( );
        baskets.forEach(basket -> {
            Integer prodCount =basket.getProdCount();
            BigDecimal price = priceMap.get(basket.getSkuId());
            BigDecimal oneMoney = price.multiply(new BigDecimal(prodCount.toString()));
            oneMoneys.add(oneMoney );
        });
        BigDecimal totalMoney =oneMoneys.stream().reduce(BigDecimal::add).get();
        // 如果总价小于99  有6元运费
        cartMoneyVO.setTotalMoney(totalMoney);
        cartMoneyVO.setFinalMoney(totalMoney);
        if (totalMoney.compareTo(new BigDecimal("99")) < 0) {
            cartMoneyVO.setTransMoney(new BigDecimal("6"));
            cartMoneyVO.setFinalMoney(totalMoney.add(new BigDecimal("6")));
        }
        return cartMoneyVO;
    }
}

package com.mingyun.money.handler;

import com.mingyun.model.OrderMoneyContext;
import com.mingyun.model.SkuBuyModel;
import com.mingyun.money.CalculateAmount;
import com.mingyun.money.CalculateAmount;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-13 11:41
 * 1
 */
@Component("prodMoneyHandler")
public class ProdMoneyHandler extends CalculateAmount {

    @Override
    public void calculate(OrderMoneyContext orderMoneyContext) {
        List<SkuBuyModel> skuBuyModels = orderMoneyContext.getSkuBuyModels();
        List<BigDecimal> oneMoneys = new ArrayList<>(skuBuyModels.size());
        skuBuyModels.forEach(skuBuyModel -> oneMoneys.add(skuBuyModel.getPrice().multiply(new BigDecimal(skuBuyModel.getCount()))));
        // 商品总价
        BigDecimal totalMoney = oneMoneys.stream().reduce(BigDecimal::add).get();
        orderMoneyContext.setTotalMoney(totalMoney);
        // 执行下一个责任链
        CalculateAmount nextHandler = getNextHandler();
        if (!ObjectUtils.isEmpty(nextHandler)) {
            nextHandler.calculate(orderMoneyContext);
        }
    }
}

package com.mingyun.money.handler;

import com.mingyun.model.OrderMoneyContext;
import com.mingyun.money.CalculateAmount;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
@Component("actualMoneyHandler")
public class ActualMoneyHandler extends CalculateAmount {

    // 总价
    @Override
    public void calculate(OrderMoneyContext orderMoneyContext) {
        BigDecimal totalMoney = orderMoneyContext.getTotalMoney();
        BigDecimal couponMoney = orderMoneyContext.getCouponMoney();
        BigDecimal shopReduce = orderMoneyContext.getShopReduce();
        BigDecimal transfee = orderMoneyContext.getTransfee();
        BigDecimal actualMoney = totalMoney.add(couponMoney).add(shopReduce).add(transfee);
        orderMoneyContext.setActualTotal(actualMoney);
        // 执行下一个责任链
        CalculateAmount nextHandler = getNextHandler();
        if (!ObjectUtils.isEmpty(nextHandler)) {
            nextHandler.calculate(orderMoneyContext);
        }

    }
}

package com.mingyun.money.handler;

import com.mingyun.model.OrderMoneyContext;
import com.mingyun.money.CalculateAmount;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-13 11:41
 */
@Component("transMoneyHandler")
public class TransMoneyHandler extends CalculateAmount {

    //  查询运费的设定 满99 包邮

    @Override
    public void calculate(OrderMoneyContext orderMoneyContext) {
        // 金额 是由 + -
        BigDecimal totalMoney = orderMoneyContext.getTotalMoney();
        BigDecimal couponMoney = orderMoneyContext.getCouponMoney();
        BigDecimal shopReduce = orderMoneyContext.getShopReduce();
        BigDecimal unTransMoney = totalMoney.add(couponMoney).add(shopReduce);
        if (unTransMoney.compareTo(new BigDecimal("99")) < 0) {
            orderMoneyContext.setTransfee(new BigDecimal("6"));
        } else {
            orderMoneyContext.setTransfee(BigDecimal.ZERO);
        }
        // 执行下一个责任链
        CalculateAmount nextHandler = getNextHandler();
        if (!ObjectUtils.isEmpty(nextHandler)) {
            nextHandler.calculate(orderMoneyContext);
        }

    }
}

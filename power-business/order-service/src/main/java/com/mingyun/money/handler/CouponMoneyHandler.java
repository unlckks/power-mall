package com.mingyun.money.handler;

import com.mingyun.model.OrderMoneyContext;
import com.mingyun.money.CalculateAmount;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-13 11:41
 */
@Component("couponMoneyHandler")
public class CouponMoneyHandler extends CalculateAmount {

    @Override
    public void calculate(OrderMoneyContext orderMoneyContext) {
        List<Long> couponIds = orderMoneyContext.getCouponIds();
        if (!CollectionUtils.isEmpty(couponIds)) {
            // 查对应优惠券的信息 计算金额
        } else {
            orderMoneyContext.setCouponMoney(BigDecimal.ZERO);
        }
        // 执行下一个责任链
        CalculateAmount nextHandler = getNextHandler();
        if (!ObjectUtils.isEmpty(nextHandler)) {
            nextHandler.calculate(orderMoneyContext);
        }
    }
}

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
@Component("reduceMoneyHandler")
public class ReduceMoneyHandler extends CalculateAmount {

    // 查询当前的活动信息
    @Override
    public void calculate(OrderMoneyContext orderMoneyContext) {

        orderMoneyContext.setShopReduce(BigDecimal.ZERO);

        // 执行下一个责任链
        CalculateAmount nextHandler = getNextHandler();
        if (!ObjectUtils.isEmpty(nextHandler)) {
            nextHandler.calculate(orderMoneyContext);
        }
    }
}

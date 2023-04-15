package com.mingyun.money;

import com.mingyun.model.OrderMoneyContext;

/**
 * @Author: MingYun
 * @Date: 2023-04-13 15:22
 */
public abstract class CalculateAmount {

    private CalculateAmount nextHandler;

    /**
     * skuBuyModels
     * openId
     * -----------
     * totalMoney 单品金额
     * // activityMoney 活动金额
     * couponMoney 优惠金额
     * transMoney 运费金额
     * actualMoney 总金额
     */
    public abstract void calculate(OrderMoneyContext orderMoneyContext);

    public CalculateAmount getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(CalculateAmount nextHandler) {
        this.nextHandler = nextHandler;
    }
}

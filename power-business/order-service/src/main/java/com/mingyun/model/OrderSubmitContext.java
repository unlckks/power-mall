package com.mingyun.model;

import com.mingyun.dto.OrderSubmitDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmitContext {

    private String openId;
    private String orderSn;
    private ChangeStock changeStock;
    private OrderMoneyContext orderMoneyContext;
    private List<Long> skuIds;
    private OrderSubmitDTO orderSubmitDTO;
}

package com.mingyun.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("prod库存对象")
public class ProdStock {

    private Long prodId;
    private Integer count;

}

package com.mingyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-07 09:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommStatistics {

    private Long prodId;
    private Long allCount;
    private Long goodCount;

}

package com.mingyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommOverview {

    private Long prodId;
    private Long allCount;
    private Long goodCount;
    private Long secondCount;
    private Long badCount;

}

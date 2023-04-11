package com.mingyun.vo;

import com.mingyun.domain.IndexImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  @Author: MingYun
 *  @Date: 2023-04-07 19:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("轮播图回显对象")
public class IndexImgVO extends IndexImg {

    @ApiModelProperty("商品的名称")
    private String prodName;

    @ApiModelProperty("商品的图片")
    private String pic;


}

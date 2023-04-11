package com.mingyun.vo;

import com.mingyun.domain.ProdComm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("评论的详情返回对象")
public class ProdCommVO extends ProdComm {

    @ApiModelProperty("会员的昵称")
    private String nickName;

    @ApiModelProperty("会员的头像")
    private String pic;


}

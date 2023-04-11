package com.mingyun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-10 21:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("会员更新入参对象")
public class MemberUpdateDTO {

    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("头像")
    private String pic;
}

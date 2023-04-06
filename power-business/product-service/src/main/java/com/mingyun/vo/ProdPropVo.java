package com.mingyun.vo;

import com.mingyun.domain.ProdProp;
import com.mingyun.domain.ProdPropValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("规格和规格值返回的对象")
public class ProdPropVo extends ProdProp {
    @ApiModelProperty(value = "规格集合")
    private List<ProdPropValue> prodPropValues;
}

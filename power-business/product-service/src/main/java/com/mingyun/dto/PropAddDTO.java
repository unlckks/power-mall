package com.mingyun.dto;

import com.mingyun.domain.ProdPropValue;
import com.mingyun.domain.ProdProp;
import com.mingyun.domain.ProdPropValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-06 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("规格查询对象")
@Validated
public class PropAddDTO {

    @ApiModelProperty("规格名称")
    @NotBlank(message = "规格名称不能为空")
    private String propName;
    @ApiModelProperty("规格值集合")
    private List<ProdPropValue> prodPropValues;


}

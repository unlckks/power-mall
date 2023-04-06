package com.mingyun.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  @Author: MingYun
 *  @Date: 2023-04-06 09:44
 */
@ApiModel(description="prod_prop_value")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "prod_prop_value")
public class ProdPropValue implements Serializable {
    /**
     * 属性值ID
     */
    @TableId(value = "value_id", type = IdType.AUTO)
    @ApiModelProperty(value="属性值ID")
    private Long valueId;

    /**
     * 属性值名称
     */
    @TableField(value = "prop_value")
    @ApiModelProperty(value="属性值名称")
    private String propValue;

    /**
     * 属性ID
     */
    @TableField(value = "prop_id")
    @ApiModelProperty(value="属性ID")
    private Long propId;

    private static final long serialVersionUID = 1L;

    public static final String COL_VALUE_ID = "value_id";

    public static final String COL_PROP_VALUE = "prop_value";

    public static final String COL_PROP_ID = "prop_id";
}
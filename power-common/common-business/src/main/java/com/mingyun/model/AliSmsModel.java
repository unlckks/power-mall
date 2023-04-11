package com.mingyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliSmsModel {

    private String phoneNumber;
    private String signName;
    private String TemplateCode;
    private String TemplateParam;

}

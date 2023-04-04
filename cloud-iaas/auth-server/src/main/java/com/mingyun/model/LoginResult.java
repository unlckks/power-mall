package com.mingyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {

    private String accessToken;

    private Long expiresIn;


}

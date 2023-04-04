package com.mingyun.ex;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 18:43
 */
public class BusinessException  extends RuntimeException{

    public BusinessException(String msg) {
        super(msg);
    }
}

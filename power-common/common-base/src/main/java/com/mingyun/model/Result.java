package com.mingyun.model;

import com.mingyun.constant.BusinessEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code = 200;

    private String msg = "ok";

    private T data;

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    /**
     * 失败
     *
     * @param businessEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(BusinessEnum businessEnum) {
        Result<T> result = new Result<>();
        result.setCode(businessEnum.getCode());
        result.setMsg(businessEnum.getDesc());
        return result;
    }
    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result<String> handle(boolean flag) {
        if (flag) {
            return success(null);
        }
        return fail(BusinessEnum.OPERATION_FAIL);
    }

}

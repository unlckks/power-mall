package com.mingyun.constant;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 13:55
 */
public enum BusinessEnum {

    UN_AUTHORIZATION(401, "未授权"),
    OPERATION_FAIL(-1, "操作失败"),
    ACCESS_DENY_FAIL(-1, "权限不足，请联系管理员"),
    SERVER_INNER_ERROR(-1, "服务器出小差了");

    private Integer code;
    private String desc;

    BusinessEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

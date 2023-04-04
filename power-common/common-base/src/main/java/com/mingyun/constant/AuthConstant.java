package com.mingyun.constant;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 11:20
 */
public interface AuthConstant {

    /**
     * token携带请求中的header：key
     */
    String AUTHORIZATION = "Authorization";

    /**
     * token的前缀
     */
    String BEARER = "bearer ";

    /**
     * redis中的token
     */
    String LOGIN_TOKEN_PREFIX = "login_token";

    /**
     * 登录的路径
     */
    String LOGIN_URL = "/doLogin";

    /**
     * 登出的路径
     */
    String LOGOUT_URL = "/doLogout";

    /**
     * 登录的类型
     */
    String LOGIN_TYPE = "loginType";

    /**
     * 管理员登录标记
     */
    String SYS_USER_LOGIN = "sysUserLogin";

    /**
     * 会员标记
     */
    String MEMBER_LOGIN = "memberLogin";


    /**
     * token的过期时间
     */
    Long TOKEN_TIME = 7200L;

    /**
     * token过期的临界值
     */
    Long TOKEN_EXPIRE_THRESHOLD_TIME = 1800L;
}

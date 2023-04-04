package com.mingyun.utils;

import com.mingyun.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 11:05
 */
public class AuthUtil {
    public static SecurityUser getLoginUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    public static Long getShopId() {
        return getLoginUser().getShopId();
    }

    public static Set<String> getPerms() {
        return getLoginUser().getPerms();
    }

}

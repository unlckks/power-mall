package com.mingyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:04
 * 登录信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;
    private String loginType;
    private Set<String> perms = new HashSet<>();

    // 前台会员的属性还没添加
    private String openId;

    /**
     * 在分布式结构中
     * 授权和解析 是可以分开的
     * auth-server只负责授权颁发token
     * 在具体的资源服务器中才处理权限的数据
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 在security框架中 用户的唯一身份 不能重复
     * <p>
     * 微信小程序
     *
     * @return
     */
    @Override
    public String getUsername() {
        return this.loginType + this.userId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }


    public void setPerms(Set<String> perms) {
        // 处理，
        HashSet<String> finalSet = new HashSet<>();
        perms.forEach(perm -> {
            if (perm.contains(",")) {
                String[] realPerms = perm.split(",");
                for (String realPerm : realPerms) {
                    finalSet.add(realPerm);
                }
            } else {
                finalSet.add(perm);
            }
        });
        this.perms = finalSet;
    }
}

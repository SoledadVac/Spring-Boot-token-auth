package com.example.demo.authorize.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:32
 * \* Description: 为了安全服务的User，实现spring security的类
 * \
 */
public class JwtUser  implements UserDetails {

    private final Long id;
    private final String username; //设置为account
    private final String mobile;//手机号
    private final String password;
    private final int status;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long id, String username, String mobile,String password, int status, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.mobile=mobile;
        this.password = password;
        this.status=status;
        this.authorities = authorities;
    }

    /**
     * 返回分配给用户的角色列表
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    /**
     * 账户是否未过期

     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        if(this.status==0){//锁定
            return false;
        }
        return true;
    }

    /**
     * 密码是否未过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }



}

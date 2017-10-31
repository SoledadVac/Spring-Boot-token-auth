package com.example.demo.TokenAuthorize.Authorize;

import com.example.demo.TokenAuthorize.User.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:43
 * \* Description: factory:根据User创建JwtUser
 * \
 */
public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getUserId(),
                user.getAccount(),//account是唯一的
                user.getPwd(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}

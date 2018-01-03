package com.example.demo.authorize.service;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.authorize.reponse.AuthorizeTokenReponse;
import com.example.demo.authorize.user.JwtUser;
import com.example.demo.authorize.user.User;
import com.example.demo.authorize.util.AuthorizeResultEnum;
import com.example.demo.authorize.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 下午2:31
 * \* Description:权限有关服务实现
 * \
 */

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登陆
     * @param useraccount
     * @param password
     * @return
     */
    @Override
    public AuthorizeTokenReponse login(String useraccount, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(useraccount, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
         JwtUser userDetails = (JwtUser)userDetailsService.loadUserByUsername(useraccount);
        if(logger.isDebugEnabled()){
            logger.debug("AuthServiceImpl_login [/auth/login] 用户登陆，根据用户名查询到用户:"+ JSONObject.toJSONString(userDetails));
        }
        User user=new User();
        user.setUserId(userDetails.getId());
        user.setUserName(userDetails.getUsername());
        user.setAccount(userDetails.getMobile());
        final String token = jwtUtil.generateToken(userDetails);
        AuthorizeResultEnum authorizeResultEnum= AuthorizeResultEnum.LOGIN_IN_SUCCEED;
        AuthorizeTokenReponse authorizeTokenReponse=new AuthorizeTokenReponse(token,user);
        authorizeTokenReponse.setErrorcode(authorizeResultEnum.getCode());
        authorizeTokenReponse.setErrorinfo(authorizeResultEnum.getMsg());
        return authorizeTokenReponse;
    }

    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        if (jwtUtil.canTokenBeRefreshed(token)){
            return jwtUtil.refreshToken(token);
        }
        return null;
    }





}

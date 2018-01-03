package com.example.demo.authorize.service;


import com.example.demo.authorize.reponse.AuthorizeTokenReponse;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 下午2:31
 * \* Description: 与认证授权有关的服务
 * \
 */
public interface AuthService {
    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    AuthorizeTokenReponse login(String username, String password);

    /**
     * 刷新 token
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);
}

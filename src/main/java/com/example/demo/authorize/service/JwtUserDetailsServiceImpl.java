package com.example.demo.authorize.service;

import com.alibaba.fastjson.JSONObject;
import com.woasis.esbp.battery.admin.author.remote.SysAuthRemote;
import com.woasis.esbp.battery.admin.authorize.reponse.AuthorLoginReponse;
import com.woasis.esbp.battery.admin.authorize.user.JwtUser;
import com.woasis.esbp.battery.admin.authorize.user.JwtUserFactory;
import com.woasis.esbp.battery.admin.authorize.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:54
 * \* Description: 提供一种从用户名可以查到用户并返回的方法
 * \
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);

    @Autowired
    private SysAuthRemote sysAuthRemote;
    /**
     * 提供一种从用户名可以查到用户并返回的方法【本系统使用手机号account进行唯一用户验证】
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public JwtUser loadUserByUsername(String account) throws UsernameNotFoundException {
        logger.debug("JwtUserDetailsServiceImpl_loadUserByUsername  查询用户，account="+account);
        String loginResult=sysAuthRemote.login(account);
        logger.debug("JwtUserDetailsServiceImpl_loadUserByUsername  查询用户，接口返回数据="+loginResult);
        AuthorLoginReponse loginReponse= JSONObject.parseObject(loginResult,AuthorLoginReponse.class);
        User user =loginReponse.getData();
        return JwtUserFactory.create(user);
    }
}

package com.example.demo.authorize.service;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.authorize.reponse.AuthorLoginReponse;
import com.example.demo.authorize.user.JwtUser;
import com.example.demo.authorize.user.JwtUserFactory;
import com.example.demo.authorize.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

   /* @Autowired
    private SysAuthRemote sysAuthRemote;*/
    /**
     * 提供一种从用户名可以查到用户并返回的方法【本系统使用手机号account进行唯一用户验证】
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public JwtUser loadUserByUsername(String account) throws UsernameNotFoundException {
        logger.debug("JwtUserDetailsServiceImpl_loadUserByUsername  查询用户，account="+account);
        //String loginResult=sysAuthRemote.login(account);
        //String loginResult="";
        //logger.debug("JwtUserDetailsServiceImpl_loadUserByUsername  查询用户，接口返回数据="+loginResult);
        //AuthorLoginReponse loginReponse= JSONObject.parseObject(loginResult,AuthorLoginReponse.class);
        //User user =loginReponse.getData();

        /**模拟根据用户表示查询用户的过程**/
        User user =new User();
        user.setUserId(1l);
        user.setUserName("pbc");
        user.setPwd("$2a$10$0aTuTVxSw1kKW/qRwOip/ObfnZLd7PcjkEUELiCEEqE.kvvrOQFeu");//明文为：
        user.setAccount("110");//手机号
        List<String> roles=new ArrayList<>();
       // roles.add("admin");//添加admin角色
        roles.add("guest");//添加guest角色
        user.setStatus(1);
        user.setRoles(roles);
        return JwtUserFactory.create(user);
    }
}

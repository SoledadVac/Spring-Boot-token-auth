package com.example.demo.authorize.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.authorize.reponse.AuthorizeReponse;
import com.example.demo.authorize.reponse.AuthorizeTokenReponse;
import com.example.demo.authorize.request.JwtAuthenticationRequest;
import com.example.demo.authorize.service.AuthService;
import com.example.demo.authorize.util.AuthorizeResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/11/9
 * \* Time: 上午11:22
 * \* Description: 与用户登陆授权有关的接口访问
 * \
 */
@RestController
@RequestMapping(value="/auth")
public class AuthorizeController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Autowired
    private AuthService authService;

    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String login(@RequestBody JwtAuthenticationRequest authenticationRequest){
        if(logger.isInfoEnabled()){
            logger.info("AuthorizeController_login [/auth/login] 用户登陆:"+JSONObject.toJSONString(authenticationRequest));
        }
        final AuthorizeTokenReponse reponse = authService.login(authenticationRequest.getUseraccount(), authenticationRequest.getPassword());
        if(logger.isInfoEnabled()){
            logger.info("AuthorizeController_login [/auth/login] 用户获取token:"+JSONObject.toJSONString(reponse));
        }
        return JSONObject.toJSONString(reponse);
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value ="/refreshtoken", method = RequestMethod.GET)
    public String refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        AuthorizeResultEnum authorizeResultEnum=null;
        if(logger.isInfoEnabled()){
            logger.info("AuthorizeController_refreshAndGetAuthenticationToken  [/auth/refreshtoken]  用户获取刷新token,原token为:"+JSONObject.toJSONString(token)+" ； 新生成token:" +JSONObject.toJSONString(refreshedToken));
        }
        if(refreshedToken == null) {
            AuthorizeReponse authorizeTokenReponse=null;
            authorizeResultEnum=AuthorizeResultEnum.REFRESH_TOKEN_FAILED;
            authorizeTokenReponse=new AuthorizeReponse(authorizeResultEnum.getCode(),authorizeResultEnum.getMsg());
            if(logger.isDebugEnabled()){
                logger.debug("AuthorizeController_refreshAndGetAuthenticationToken [/auth/refreshtoken] token刷新失败");
            }
            return JSONObject.toJSONString(authorizeTokenReponse);
        } else {
            authorizeResultEnum=AuthorizeResultEnum.REFRESH_TOKEN_SUCCEED;
             AuthorizeTokenReponse authorizeTokenReponse=new AuthorizeTokenReponse(token,null);
             authorizeTokenReponse.setErrorcode(authorizeResultEnum.getCode());
             authorizeTokenReponse.setErrorinfo(authorizeResultEnum.getMsg());
             if(logger.isDebugEnabled()){
                 logger.debug("AuthorizeController_refreshAndGetAuthenticationToken [/auth/refreshtoken] token刷新成功");
             }
            return JSONObject.toJSONString(authorizeTokenReponse);
        }

    }

    /**
     * 测试token
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = {"/test"},method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String test(@RequestBody JwtAuthenticationRequest authenticationRequest){
        return JSONObject.toJSONString(authenticationRequest);
    }
}

package com.example.demo.authorize.exception;

import com.alibaba.fastjson.JSONObject;
import com.woasis.esbp.battery.admin.authorize.reponse.AuthorizeReponse;
import com.woasis.esbp.battery.admin.authorize.util.AuthorizeResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * jwt 未授权 异常处理
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        logger.info("JwtAuthenticationEntryPoint[commence] 拦截到登陆授权异常："+JSONObject.toJSONString(authException));
        AuthorizeResultEnum authorizeResultEnum;
        AuthorizeReponse reponse;
        if(authException instanceof InternalAuthenticationServiceException){/**用户不存在**/
            authorizeResultEnum=AuthorizeResultEnum.USER_NOT_EXIST;
            reponse=new AuthorizeReponse(authorizeResultEnum.getCode(),authorizeResultEnum.getMsg());
            response.getWriter().write(JSONObject.toJSONString(reponse));
        } else if(authException instanceof BadCredentialsException){ /**密码不正确*/
            authorizeResultEnum=AuthorizeResultEnum.PASSWORD_IS_WRONG;
            reponse=new AuthorizeReponse(authorizeResultEnum.getCode(),authorizeResultEnum.getMsg());
            response.getWriter().write(JSONObject.toJSONString(reponse));
        }else if(authException instanceof LockedException){/**账号被禁用**/
            authorizeResultEnum=AuthorizeResultEnum.USER_IS_DISABLED;
            reponse=new AuthorizeReponse(authorizeResultEnum.getCode(),authorizeResultEnum.getMsg());
            response.getWriter().write(JSONObject.toJSONString(reponse));

        }else{       /**token认证未通过**/
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的token");
        }
    }
}

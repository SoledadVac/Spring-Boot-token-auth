package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.TokenAuthorize.Authorize.AuthService;
import com.example.demo.TokenAuthorize.Authorize.JwtAuthenticationRequest;
import com.example.demo.TokenAuthorize.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/24
 * \* Time: 下午5:03
 * \* Description:
 * \
 */
@RestController
public class HelloController {

    @Autowired
    private AuthService authService;

    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "/auth/login",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String login(@RequestBody JwtAuthenticationRequest authenticationRequest){
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        JSONObject  result=new JSONObject();
        result.put("token",token);
        return JSONObject.toJSONString(result);
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value ="/auth/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            JSONObject  result=new JSONObject();
            result.put("token",token);
            return  ResponseEntity.ok(result);
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

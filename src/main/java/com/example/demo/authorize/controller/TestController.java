package com.example.demo.authorize.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.authorize.request.JwtAuthenticationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * \* Created: liuhuichao
 * \* Date: 2018/1/15
 * \* Time: 上午10:36
 * \* Description:
 * \
 */
@RestController
@RequestMapping(value="/test")
public class TestController {

    /**
     * 测试token
     * @param authenticationRequest
     * @return
     */
    @PreAuthorize("hasRole('admin')")//后端判断用户角色，如果没有指定角色，禁止访问接口
    @RequestMapping(value = {"/index"},method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String test(@RequestBody JwtAuthenticationRequest authenticationRequest){
        return JSONObject.toJSONString(authenticationRequest);
    }
}

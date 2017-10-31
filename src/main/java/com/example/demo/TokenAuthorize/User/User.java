package com.example.demo.TokenAuthorize.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/27
 * \* Time: 下午3:14
 * \* Description:
 * \
 */
public class User implements Serializable {


    private String account;//一般为手机号
    private String pwd;//账号密码
    private Long userId;//用户id
    private List<String> roles;//角色


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}


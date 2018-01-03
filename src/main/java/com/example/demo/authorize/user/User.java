package com.example.demo.authorize.user;

import java.io.Serializable;
import java.util.List;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/27
 * \* Time: 下午3:14
 * \* Description: 对应系统的模型
 * \
 */
public class User implements Serializable {


    private Long userId;//用户id
    private String userName;//用户名
    private String account;//一般为手机号
    private String pwd;//账号密码
    private int status;//状态 0表示被禁用；1表示启用
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


package com.example.demo.authorize.util;


/**
 * \* Created: liuhuichao
 * \* Date: 2017/11/10
 * \* Time: 上午11：38
 * \* Description: 登录授权代码
 * \
 */
public enum AuthorizeResultEnum {


    /**用户不存在**/
    USER_NOT_EXIST(8001, "用户不存在"),

    /**密码错误**/
    PASSWORD_IS_WRONG(8002, "密码错误"),

    /**登陆成功**/
    LOGIN_IN_SUCCEED(8003, "登陆成功"),

    /**token刷新成功**/
    REFRESH_TOKEN_SUCCEED(8004, "token刷新成功"),

    /**token刷新失败**/
    REFRESH_TOKEN_FAILED(8005, "token刷新失败"),

    /**账号被禁用**/
    USER_IS_DISABLED(8006,"账号被禁用");


    private String msg;
    private Integer code;

    AuthorizeResultEnum(Integer code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public static String getName(int code) {
        for (AuthorizeResultEnum e : AuthorizeResultEnum.values()) {
            if (e.getCode() == code) {
                return e.getMsg();
            }
        }
        return null;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}

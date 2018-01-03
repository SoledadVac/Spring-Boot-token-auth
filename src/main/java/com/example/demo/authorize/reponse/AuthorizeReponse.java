package com.example.demo.authorize.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/11/10
 * \* Time: 上午11:38
 * \* Description:登录授权返回状态码
 * \
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeReponse {
    private Integer errorcode;
    private String errorinfo;
}

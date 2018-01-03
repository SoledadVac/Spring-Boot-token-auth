package com.example.demo.authorize.reponse;

import com.example.demo.authorize.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/11/10
 * \* Time: 上午11:40
 * \* Description:返回token数据
 * \
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeTokenReponse  extends AuthorizeReponse{
    private String token;
    private User user;
}

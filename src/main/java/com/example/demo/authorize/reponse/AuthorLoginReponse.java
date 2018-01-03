package com.example.demo.authorize.reponse;

import com.example.demo.authorize.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created: liuhuichao
 * \* Date: 2017/11/30
 * \* Time: 下午3:20
 * \* Description:访问登陆接口返回数据
 * \
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorLoginReponse {
    private long code;
    private String msg;
    private User data;
}

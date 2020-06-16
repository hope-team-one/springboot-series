package com.imooc.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 偷偷给用户注册  在用户点击qq登录并且我们当前系统不存在这个用户的时候执行 返回一个我们系统的userid
 */
@Component
public class DemoConnectionSignUp implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户的信息默认创建用户，并返回用户唯一标识
        return connection.getDisplayName();
    }
}

package com.imooc.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证服务器
 */
@Configuration
//用这个注解实际上已经实现了认证服务器  内部自带的
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig {
}

package com.imooc.core.authtication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 公共的SecurityConfig
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()//表单登陆
                //如果不要这个 那么就会和自定义了异常处理器一样 不进入到最后一层 直接在filter抛出异常并且由默认的异常处理器处理
                .loginPage(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL)//不直接跳转到登陆页面，跳转到controller
                //让usernamepasswordauthfilter这个类来处理这个请求，因为默认是处理login这个请求 现在这个请求就是登录请求
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(imoocAuthenticationSuccessHandler)
                //如果配置这个那么当登陆失败了就会进入到这个异常处理器并且传入异常信息返回给页面
                //如果没有配置这个 那么当登陆失败就会进入security自己的异常处理器，然后进入到我们配置的loginpage对应的方法
                .failureHandler(imoocAuthenticationFailureHandler);
    }
}

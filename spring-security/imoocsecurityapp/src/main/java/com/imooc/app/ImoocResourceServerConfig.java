package com.imooc.app;

import com.imooc.app.social.openid.OpenIdAutenticationSecurityConfig;
import com.imooc.core.authtication.SecurityConstants;
import com.imooc.core.authtication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.validatecode.ValidateCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器
 */
@Configuration
@EnableResourceServer
public class ImoocResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;
    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ValidateCodeAuthenticationSecurityConfig validateCodeAuthenticationSecurityConfig;
    @Autowired
    private OpenIdAutenticationSecurityConfig openIdAutenticationSecurityConfig;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()//表单登陆
                //如果不要这个 那么就会和自定义了异常处理器一样 不进入到最后一层 直接在filter抛出异常并且由默认的异常处理器处理
                .loginPage(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL)//不直接跳转到登陆页面，跳转到controller
                //让usernamepasswordauthfilter这个类来处理这个请求，因为默认是处理login这个请求 现在这个请求就是登录请求
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(imoocAuthenticationSuccessHandler)
                //如果配置这个那么当登陆失败了就会进入到这个异常处理器并且传入异常信息返回给页面
                //如果没有配置这个 那么当登陆失败就会进入security自己的异常处理器，然后进入到我们配置的loginpage对应的方法
                .failureHandler(imoocAuthenticationFailureHandler)
                .and()
                //验证码的配置统一放到validateCodeAuthenticationSecurityConfig里面配置
                .apply(validateCodeAuthenticationSecurityConfig)
                .and()
                //把smsCodeAuthenticationSecurityConfig里面的内容加入到当前这个配置类里面 短信验证码的登陆配置类 自定义filter和provider
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(imoocSocialSecurityConfig)//往我当前的过滤器链上加上过滤器以及配置，会拦截某些特定请求，等同于上面的apply的作用，生效filter和provider
                .and()
                .apply(openIdAutenticationSecurityConfig)
                .and()
                .authorizeRequests()//对请求做授权
                .antMatchers(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_SMS_CODE_CREATE_URL,
                        SecurityConstants.DEFAULT_IMAGE_CODE_CREATE_URL,
                        securityProperties.getBrowser().getSignUpUrl(),
                        SecurityConstants.REGIST_URL,
                        SecurityConstants.QQ_LOGIN,
                        SecurityConstants.SESSION_INVALID,
                        SecurityConstants.LOGIN_ERROR,
                        securityProperties.getBrowser().getLoginPage()).permitAll()//访问这样的url的时候不需要做认证
                .anyRequest()//任何请求
                .authenticated()//都需要认证
                .and()
                .csrf().disable();//跨站请求伪造的防护功能去掉
    }
}

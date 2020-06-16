package com.imooc.browser;

import com.imooc.browser.exception.MyAuthenticationFailHandler;
import com.imooc.browser.exception.MyAuthenticationSuccessHandler;
import com.imooc.browser.logout.ImoocLogoutSuccessHandler;
import com.imooc.browser.session.InvalidSessionStrategyImpl;
import com.imooc.browser.session.SessionInformationExpiredStrategyImpl;
import com.imooc.core.authtication.AbstractChannelSecurityConfig;
import com.imooc.core.authtication.SecurityConstants;
import com.imooc.core.authtication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.validatecode.ValidateCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    //具体配置在demo项目里面，application.properties配置文件里面
    @Autowired
    private DataSource dataSource;
    //这里可以注入我们自定义的接口类，只要这个接口继承了userdetailsservice接口就行了 传自定义接口就相当于传userde接口
    @Autowired
    private UserDetailsService userDetailsService;
    //短信验证码认证配置类
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    //校验配置类
    @Autowired
    private ValidateCodeAuthenticationSecurityConfig validateCodeAuthenticationSecurityConfig;
    //登出处理类
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//        MyDaoAuthenticationProvider dao = new MyDaoAuthenticationProvider();
//        dao.setHideUserNotFoundExceptions(false);
//        dao.setUserDetailsService(userDetailsService);
//        dao.setPasswordEncoder(passwordEncoder);
//        return dao;
//    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //启动的时候去数据库里面自动建表
//        tokenRepository.setCreateTableOnStartup(true);

        return tokenRepository;
    }

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;
//    @Autowired
//    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//        //将自己定义的登陆失败处理器传入 目的就是想在验证验证码时抛出的异常，能够被自定义的异常处理器处理
//        validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();
//
//        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
//        //将自己定义的登陆失败处理器传入 目的就是想在验证验证码时抛出的异常，能够被自定义的异常处理器处理
//        smsCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailHandler);
//        smsCodeFilter.setSecurityProperties(securityProperties);
//        smsCodeFilter.afterPropertiesSet();
        applyPasswordAuthenticationConfig(http);//公共的配置在这里生效
        http
                //验证码的配置统一放到validateCodeAuthenticationSecurityConfig里面配置
                .apply(validateCodeAuthenticationSecurityConfig)
                .and()
                //把smsCodeAuthenticationSecurityConfig里面的内容加入到当前这个配置类里面 短信验证码的登陆配置类 自定义filter和provider
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(imoocSocialSecurityConfig)//往我当前的过滤器链上加上过滤器以及配置，会拦截某些特定请求，等同于上面的apply的作用，生效filter和provider
                .and()
                //将自定义的短信验证码校验的过滤器加到整个的过滤器链上
//                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)//把自定义的过滤器放在username过滤器之前
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)//把自定义的过滤器放在username过滤器之前
//                .formLogin()//表单登录
                //如果不要这个 那么就会和自定义了异常处理器一样 不进入到最后一层 直接在filter抛出异常并且由默认的异常处理器处理
//                .loginPage("/authentication/require")//不直接跳转到登陆页面，跳转到controller
                //让usernamepasswordauthfilter这个类来处理这个请求，因为默认是处理login这个请求 现在这个请求就是登录请求
//                .loginProcessingUrl("/authentication/form")
//                .successHandler(myAuthenticationSuccessHandler)
                //如果配置这个那么当登陆失败了就会进入到这个异常处理器并且传入异常信息返回给页面
                //如果没有配置这个 那么当登陆失败就会进入security自己的异常处理器，然后进入到我们配置的loginpage对应的方法
//                .failureHandler(myAuthenticationFailHandler)
//                .and()
                //记住我的配置
                .rememberMe()
                .tokenRepository(persistentTokenRepository())//连接数据源的repository
                .tokenValiditySeconds(securityProperties.getBrowser().getRemembermeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                //当session失效以后走的方法
                .invalidSessionStrategy(invalidSessionStrategy)
                //同一个用户只能存在一个session，后面的登录会踢掉前面的session
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                .logout()
                //登陆退出的请求
                .logoutUrl("/signOut")
                //退出成功后 访问的url 顶替掉loginPage(不是securityproperties里面的loginPage)这个配置
//                .logoutSuccessUrl("/imooc.singOut.html")
                //退出逻辑自定义 与logoutSuccessUrl互斥
                .logoutSuccessHandler(logoutSuccessHandler)
                //当前得sessionid从cookie中删掉
                .deleteCookies("JSESSIONID")
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
                //有admin这个角色的用户才能访问这个请求
                .antMatchers("/user").hasRole("ADMIN")
                .anyRequest()//任何请求
                .authenticated()//都需要认证
                .and()
                .csrf().disable();//跨站请求伪造的防护功能去掉
    }
    //对于登录异常处理的配置是有优先级的
    //1.如果你定义了自定义异常处理器 那么登录出现异常就会 进入这个异常处理器
    //2.如果你没有定义自定义异常处理器，但是定义了loginpage并且指定了方法，那么登录出现异常就会走到最后一层，抛出异常，然后进入这个方法
    //3.如果你以上两个都没有定义，那么登录出现异常，就会进入默认的异常处理器，和自定义异常处理器的处理流程是一样的 都不会进入最后一层
    //如果1,2两个都定义了那么1会在登录的时候生效 2会在登录之前生效(也就是直接请求，请求没有任何信息，不会有过滤器为其生效，直接到达最后一层)
}

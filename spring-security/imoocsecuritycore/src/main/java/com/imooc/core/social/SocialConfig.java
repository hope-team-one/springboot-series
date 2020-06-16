package com.imooc.core.social;

import com.imooc.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交配置的适配器
 */
@Configuration
@EnableSocial //将springsocial社交项目相应的特性起起来
public class SocialConfig extends SocialConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor;
    @Autowired(required = false)//可能在demo项目中不使用这个
    private ConnectionSignUp connectionSignUp;
    //一定要加这两个注解，不然会导致SocialConfiguration这个类的usersConnectionRepository()这个方法返回的不是jdbcUserrep这个实现类
    //而是InMemoryUsersConnectionRepository这个实现类
    @Primary
    @Bean
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("imooc_");
        if(connectionSignUp!=null){
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 让自定义social配置生效，filter和provider还是使用自带的，改了个filter默认拦截的请求前缀 认证服务器在用户同意授权之后回调的url和请求的url一样
     * 例如用户点击了url为/auth/qq的按钮被filter拦截导向了认证服务器，那么认证服务器回调的地址就是配置的回调域加上/auth/qq
     * @return
     */
    @Bean
    public SpringSocialConfigurer imoocSocialSecurityConfig(){
        String processesUrl = securityProperties.getSocial().getProcessesUrl();
        //系统启动的时候加载，让自定义的类替代系统默认的SpringSocialConfigurer类，系统就会使用我们自定义的类里面的方法postProcess();
        //其余的还是会使用父类的，提前让processUrl存在在自定义的这个类里面，当系统调用到postProcess()方法时，将这个processUrl值存到filter类里面去
        //覆盖掉filter原来默认的值
        imoocSpringSocialConfigurer configurer = new imoocSpringSocialConfigurer(processesUrl);
        configurer.setSocialAuthenticationFilterProcessor(socialAuthenticationFilterProcessor);
        //signupurl要从demo模块的配置文件里面读取，不用social默认的
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }
    /**
     * 解决了1.在注册过程中如何拿到springsocial里面的用户信息
     * 2.注册完成了如何把业务系统的userid传给springsocial录入到userconnection表
     */
//    @Bean
//    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
//        return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
//    }
}

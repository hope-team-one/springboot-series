package com.imooc.core.social.qq.config;

import com.imooc.core.properties.QQProperties;
import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.social.qq.api.QQImpl;
import com.imooc.core.social.qq.connect.QQConnectFactory;
import com.imooc.core.social.weixin.api.WeixinImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.social.connect.ConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "imooc.security.social.qq",name = "app-id") //系统没有配置qq的appid和secret 那么这个配置不起作用
public class QQAutoConfig extends SocialAutoConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 通过配置的服务商id(qq/weixin)，我们应用在服务商那里的appid，appsecret去创建一个我们自定的工厂，在social
     * 拦截到auth开头的请求的时候，就会使用我们自定义的工厂来进行操作
     * @return
     */
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqProperties = securityProperties.getSocial().getQq();
        ///auth/qq qq这个参数要和我们properties里面配置的providerId的值一样  因为OAuth2AuthenticationService这个对象是以properties
        //配置的providerId为key存入到map的  当我们/auth/qq这个请求到达，会使用qq这个值当作key去map中取出OAuth2AuthenticationService
        //所以这个qq和properties里面配置的providerId的值必须一样 随便我们怎么定义 是系统内部的
        return new QQConnectFactory(qqProperties.getProviderId(),qqProperties.getAppId(),qqProperties.getAppSecret());
    }

}

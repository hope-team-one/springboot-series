package com.imooc.core.social.weixin.config;

import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.properties.WeixinProperties;
import com.imooc.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "imooc.security.social.weixin", name = "app-id")
public class WeixinAutoConfig extends SocialAutoConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinProperties = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinProperties.getProviderId(),weixinProperties.getAppId(),weixinProperties.getAppSecret());
    }
}

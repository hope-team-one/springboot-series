package com.imooc.core.social.qq.connect;

import com.imooc.core.social.qq.api.QQ;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class QQConnectFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     *
     * @param providerId 提供商的唯一标识 根据配置文件配置
     */
    public QQConnectFactory(String providerId, String appId , String appSecret) {
        super(providerId, new QQServiceProvider(appId,appSecret), new QQAdapter());
    }
}

package com.imooc.core.social.qq.connect;

import com.imooc.core.social.qq.api.QQ;
import com.imooc.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
    //应用注册的时候 qq互联为这个应用分配的id
    private String appId;

    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    private static final String URL_ACCESSTOKEN = "https://graph.qq.com/oauth2.0/token";
    /**
     * @param appId 不同应用的appid和appsecret是不一样的 需要在具体应用里面配
     * @param appSecret
     */
    public QQServiceProvider(String appId,String appSecret) {
        //3.将用户导向认证服务器的url 4.根据授权码申请令牌的地址
        //使我们自定义的oauthtemplate生效 自定义了postForAccessGrant方法(发送请求到qq认证服务器获取令牌，解析返回值)
        //createRestTemplate方法(自定义的创建template的方法)
        super(new QqOAuthTemplate(appId,appSecret,URL_AUTHORIZE,URL_ACCESSTOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String s) {
        return new QQImpl(s,appId);
    }
}

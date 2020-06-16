package com.imooc.core.social.weixin.connect;

import com.imooc.core.social.qq.connect.QqOAuthTemplate;
import com.imooc.core.social.weixin.api.Weixin;
import com.imooc.core.social.weixin.api.WeixinImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<Weixin> {
    /**
     * 微信获取授权码的url
     * @param accessToken
     * @return
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";

    /**
     * 微信获取令牌的url 根据授权码
     * @param accessToken
     * @return
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 使我们自定义的OAuth2Template生效，并且自定义url
     * @param appId
     * @param appSecret
     */
    public WeixinServiceProvider(String appId,String appSecret){
        super(new WeixinOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
    }

    @Override
    public Weixin getApi(String accessToken) {
        return new WeixinImpl(accessToken);
    }
}

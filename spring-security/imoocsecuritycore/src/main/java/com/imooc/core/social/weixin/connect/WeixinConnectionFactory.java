package com.imooc.core.social.weixin.connect;

import com.imooc.core.social.qq.connect.QQAdapter;
import com.imooc.core.social.qq.connect.QQServiceProvider;
import com.imooc.core.social.weixin.api.Weixin;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    public WeixinConnectionFactory(String providerId,String appId,String appSecret){
        super(providerId,new WeixinServiceProvider(appId,appSecret),new WeixinAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置poviderUserId即可，不用像qq那样通过qqimpl构造方法去获取openId
     * @param accessGrant
     * @return
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeixinAccessGrant){
            return ((WeixinAccessGrant)accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<Weixin>(getProviderId(),extractProviderUserId(accessGrant),accessGrant.getAccessToken(),accessGrant.getRefreshToken(), accessGrant.getExpireTime(),getOAuth2ServiceProvider(),getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<Weixin> createConnection(ConnectionData data) {
        return new OAuth2Connection<Weixin>(data,getOAuth2ServiceProvider(),getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<Weixin> getApiAdapter(String providerId) {
        return new WeixinAdapter(providerId);
    }

    private OAuth2ServiceProvider getOAuth2ServiceProvider(){
        return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
    }
}

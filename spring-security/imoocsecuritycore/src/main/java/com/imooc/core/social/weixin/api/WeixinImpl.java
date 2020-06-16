package com.imooc.core.social.weixin.api;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Weixin API调用模板，scope为request的Springbean，根据当前用户的accessToken创建
 */
public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取用户信息的url
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";
    public WeixinImpl(String accessToken){
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为IS0-8859-1，而微信返回的是UTF-8的，所以覆盖
     */
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

    /**
     * 获取微信用户信息  因为social在调用getapi方法的时候只会传入令牌，加上微信没有提供根据令牌获取openId的方法，是和令牌一起返回的，所以impl里面就不会存在openid
     * 所以需要往调用getuserinfo方法的类里面存入openid，那么就是adapter类，
     * @param openId
     * @return
     */
    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String URL = URL_GET_USER_INFO + openId;
        String response = getRestTemplate().getForObject(URL,String.class);
        if(StringUtils.contains(response,"errcode")){
            return null;
        }
        WeixinUserInfo weixinUserInfo =null;
        try {
            weixinUserInfo = objectMapper.readValue(response,WeixinUserInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return weixinUserInfo;
    }
}

package com.imooc.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * api实现 获取用户信息 oauth的最后一步 获取资源
 * 我们自己写一个api，然后在这个api里面 我们实现调用服务商提供的api
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ{
    //获取openid的url 需要传入令牌
    private static final String URL_GETOPENID="https://graph.qq.com/oauth2.0/me?access_token=%s";
    private static final String URL_GETUSERINFO="https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    private String appId;
    //用户的ID，与QQ号码一一对应。
    private String openId;

    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(){

    }

    /**
     * @param accessToken 从认证服务器获取到的令牌
     * @param appId 系统的配置信息 应用在服务商上面注册的id
     */
    public QQImpl(String accessToken, String appId){
        //为了将请求的参数放到url中 而不是请求头中
        super(accessToken, TokenStrategy.OAUTH_TOKEN_PARAMETER);
        this.appId = appId;
        //拼装请求 用于通过accesstoken获取的openid 不要问我为什么 文档规定的这个请求可以获取到openid也就是用户的qq号
        String url = String.format(URL_GETOPENID,accessToken);
        String result = getRestTemplate().getForObject(url,String.class);
        System.out.println(result);
        //服务商上用户的id 也就是qq号
        this.openId = StringUtils.substringBetween(result,"\"openid\":\"","\"}");
    }

    /**
     * OAuth中最后一步 获取用户资源(信息)  到系统中登陆
     * @return
     */
    @Override
    public QQUserInfo getUserInfo(){
        //拼装获取用户信息的请求
        String url = String.format(URL_GETUSERINFO,appId,openId);
        String result = getRestTemplate().getForObject(url,String.class);
        System.out.println(result);
        //把json格式的字符串转成指定的java对象
        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result,QQUserInfo.class);
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败");
        }
    }
}

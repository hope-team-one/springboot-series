package com.imooc.core.social.weixin.connect;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

public class WeixinOAuth2Template extends OAuth2Template {

    private String clientId;

    private String clientSecret;

    private String accessTokenUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    private Logger logger = LoggerFactory.getLogger(getClass());

    public WeixinOAuth2Template(String clientId,String clientSecret,String authorizeUrl,String accessTokenUrl){
        super(clientId,clientSecret,authorizeUrl,accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }

    /**
     * exchangeForAccess(),refreshAccess(),buildAuthenticateUrl()这三个方法的目的就是为了
     * 满足微信url的参数规范，因为微信的参数名称和OAuth协议中的参数名称不一样，所以需要改
     * @param authorizationCode
     * @param redirectUri
     * @param additionalParameters
     * @return
     */
    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);

        accessTokenRequestUrl.append("?appId="+clientId);
        accessTokenRequestUrl.append("&secret="+clientSecret);
        accessTokenRequestUrl.append("&code="+authorizationCode);
        accessTokenRequestUrl.append("&grant_type=authorization_code");
        accessTokenRequestUrl.append("&redirect_url="+redirectUri);
        return getAccessToken(accessTokenRequestUrl);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);
        refreshTokenUrl.append("?appid="+clientId);
        refreshTokenUrl.append("&grant_type=refresh_token");
        refreshTokenUrl.append("&refresh_token="+refreshToken);
        return getAccessToken(refreshTokenUrl);
    }
    @SuppressWarnings("unchecked")
    private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl){
        logger.info("获取access_token，请求URL："+accessTokenRequestUrl.toString());
        String response = getRestTemplate().getForObject(accessTokenRequestUrl.toString(),String.class);
        logger.info("获取access_token，响应内容："+response);
        Map<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(response,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        //返回错误码时，直接返回空
        if(StringUtils.isNotBlank(MapUtils.getString(result,"errcode"))){
            String errcode = MapUtils.getString(result,"errcode");
            String errmsg = MapUtils.getString(result,"errmsg");
            throw new RuntimeException("获取access token失败，errcode:"+errcode+",errmsg:"+errmsg);
        }
        WeixinAccessGrant accessGrant = new WeixinAccessGrant(
                MapUtils.getString(result,"access_token"),
                MapUtils.getString(result,"scope"),
                MapUtils.getString(result,"refresh_token"),
                MapUtils.getLong(result,"expires_in")
        );
        accessGrant.setOpenId(MapUtils.getString(result,"openid"));
        return accessGrant;
    }

    /**
     * 构建获取授权码的请求，也就是引导用户跳转到微信的地址
     * @param parameters
     * @return
     */
    @Override
    public String buildAuthenticateUrl(OAuth2Parameters parameters) {
        String url = super.buildAuthenticateUrl(parameters);
        url = url + "&appid="+clientId+"&scope=snsapi_login";
        return url;
    }

    @Override
    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        return buildAuthenticateUrl(parameters);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        //新加一个UTF-8的messageconverter，不然不能处理返回的值
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}

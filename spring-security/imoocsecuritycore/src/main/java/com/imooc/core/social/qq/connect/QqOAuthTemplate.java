package com.imooc.core.social.qq.connect;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

public class QqOAuthTemplate extends OAuth2Template {
    public QqOAuthTemplate(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        //设置为true发请求才会带上client_id和client_secret
        super.setUseParametersForClientAuthentication(true);
    }
    /**
     * 覆盖social自己发送请求获取令牌的方法  使用我们自己的 并且返回一个string类型
     * 然后按照qq的解析标准来解析获取参数
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl,parameters,String.class);
        System.out.println(responseStr);
        String [] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr,"&");
        String accessToken = StringUtils.substringAfterLast(items[0],"=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1],"="));
        String refreshToken = StringUtils.substringAfterLast(items[2],"=");

        return new AccessGrant(accessToken,null,refreshToken,expiresIn);
    }

    /**
     * 不这样写在获取令牌的时候会报错 默认注册的StringHttpMessageConverter字符集是IS0-8859-1，不是UTF-8
     * @return
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        //新加一个UTF-8的messageconverter，不然不能处理返回的值
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}

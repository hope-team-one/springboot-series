package com.imooc.core.social.weixin.connect;

import org.springframework.social.oauth2.AccessGrant;

public class WeixinAccessGrant extends AccessGrant {
    private static final long serialVersionUID = -7243374526633186782L;
    /**
     * 标准的OAuth协议是不会在获取accessToken的时候返回openId，而是在获取到accessToken之后再根据accessToken获取到openId
     * 但是微信是在获取accessToken的时候就返回了openId，所以需要新加一个openId字段
     */
    private String openId;
    public WeixinAccessGrant(){
        super("");
    }
    public WeixinAccessGrant(String accessToken) {
        super(accessToken);
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

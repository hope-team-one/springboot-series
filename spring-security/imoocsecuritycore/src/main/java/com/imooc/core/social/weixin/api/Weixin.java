package com.imooc.core.social.weixin.api;

public interface Weixin {
    /**
     * 微信和qq不同，不需要拿accessToken换openId
     * @param openId
     * @return
     */
    WeixinUserInfo getUserInfo(String openId);
}

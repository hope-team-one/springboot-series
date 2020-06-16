package com.imooc.core.social.weixin.connect;

import com.imooc.core.social.weixin.api.Weixin;
import com.imooc.core.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信api适配器，将微信api的数据模型转为springsocial的标准模型
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {
    private String openId;
    public WeixinAdapter(){

    }
    public WeixinAdapter(String openId){
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin api) {
        return true;
    }

    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenId());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {

    }
}

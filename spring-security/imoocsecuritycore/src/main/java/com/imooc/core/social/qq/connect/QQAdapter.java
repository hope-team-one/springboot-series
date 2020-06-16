package com.imooc.core.social.qq.connect;

import com.imooc.core.social.qq.api.QQ;
import com.imooc.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 当前qq服务是否可用
     * @param qq
     * @return
     */
    @Override
    public boolean test(QQ qq) {
        return true;//认为永远是通的
    }

    /**
     * 适配 把connectionvalues需要的数据设置上去 通过qq
     * @param qq
     * @param connectionValues
     */
    @Override
    public void setConnectionValues(QQ qq, ConnectionValues connectionValues) {
            QQUserInfo qqUserInfo = qq.getUserInfo();//获取到qq用户的信息
            connectionValues.setDisplayName(qqUserInfo.getNickname());
            connectionValues.setImageUrl(qqUserInfo.getFigureurl_qq_1());
            connectionValues.setProfileUrl(null);//个人主页
            connectionValues.setProviderUserId(qqUserInfo.getOpenId());//服务商的用户id  也就是我们这个系统的用户在服务商上面的唯一标识 例如qq就是qq号
    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    @Override
    public void updateStatus(QQ qq, String s) {

    }
}

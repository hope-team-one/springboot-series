package com.imooc.core.properties;

/**
 * 短信
 */
public class SmsCodeProperties {
    //如果用户在配置文件里面配置了值，那么这里的默认值就会被用户配置的值覆盖 优先使用用户配置的值
    private int length = 6;
    private int expireIn = 60;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}

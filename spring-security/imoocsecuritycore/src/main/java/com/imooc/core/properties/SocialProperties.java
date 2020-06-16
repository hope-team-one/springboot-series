package com.imooc.core.properties;

public class SocialProperties {

    private QQProperties qq = new QQProperties();
    private String filterProcessesUrl = "/auth";
    private WeixinProperties weixin = new WeixinProperties();

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }
    public String getProcessesUrl() {
        return filterProcessesUrl;
    }

    public void setProcessesUrl(String processesUrl) {
        this.filterProcessesUrl = processesUrl;
    }

    public WeixinProperties getWeixin() {
        return weixin;
    }

    public void setWeixin(WeixinProperties weixin) {
        this.weixin = weixin;
    }
}

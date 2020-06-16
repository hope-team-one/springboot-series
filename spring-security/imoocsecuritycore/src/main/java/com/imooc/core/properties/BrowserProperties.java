package com.imooc.core.properties;

public class BrowserProperties {

    private String loginPage = "/imooc-signIn.html";

    private int remembermeSeconds = 3600;

    private String signUpUrl = "/imooc-signUp.html";
    //默认为空 登出成功之后访问的url 由用户配置 如果用户配置了才会在登出成功之后访问这个url
    private String signOutUrl;
    public int getRemembermeSeconds() {
        return remembermeSeconds;
    }

    public void setRemembermeSeconds(int remembermeSeconds) {
        this.remembermeSeconds = remembermeSeconds;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }
}

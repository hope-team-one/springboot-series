package com.imooc.browser;

public enum  LoginType {
    JSON("/imooc-signIn.html");
    private String type;

    LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

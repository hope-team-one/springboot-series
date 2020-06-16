package com.imooc.core.validatecode;

public enum ValidateCodeType {
    IMAGE_CODE_TYPE("image"),
    SMS_CODE_TYPE("sms")
    ;
    private String type;
    private ValidateCodeType(){

    }
    private ValidateCodeType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

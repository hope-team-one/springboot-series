package com.imooc.browser.repository;

import com.imooc.core.repository.ValidateCodeRepository;
import com.imooc.core.validatecode.ValidateCode;
import com.imooc.core.validatecode.ValidateCodeType;
import com.imooc.core.validatecode.image.ImageCode;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {
    //验证码放入session时的前缀
    public static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        sessionStrategy.setAttribute(request,getSessionKey(request,type),code);
    }

    private String getSessionKey(ServletWebRequest request,ValidateCodeType validateCodeType){
        return SESSION_KEY_PREFIX+validateCodeType.toString().toUpperCase();
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
        return (ValidateCode)sessionStrategy.getAttribute(request,getSessionKey(request,type));
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType type) {
        sessionStrategy.removeAttribute(request, getSessionKey(request,type));
    }

//    public static void main(String[] args) {
//        ImageCode i = new ImageCode();
//        i.setExpireTime(LocalDateTime.now());
//        i.setCode("5258");
//        ValidateCode validateCode = (ValidateCode)i;
//        ValidateCode validateCode = new ValidateCode();
//        validateCode.setExpireTime(LocalDateTime.now());
//        validateCode.setCode("2050");
//        ImageCode imageCode = (ImageCode)validateCode;
//    }
}
